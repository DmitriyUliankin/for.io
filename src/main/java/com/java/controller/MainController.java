package com.java.controller;

import com.java.domain.Comment;
import com.java.domain.Message;
import com.java.domain.User;
import com.java.domain.dto.MessageDto;
import com.java.repository.MessageRepository;
import com.java.service.CommentService;
import com.java.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String slash() {
        return "slash";
    }

    @GetMapping("/home")
    public String greeting(@RequestParam(required = false, defaultValue = "") String filter,
                           Model model,
                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                           @AuthenticationPrincipal User user) {
        Page<MessageDto> page = messageService.messageList(pageable, filter, user);
        List<Comment> list = commentService.findAll();
        model.addAttribute("comment", list);

        PageImpl<MessageDto> messageDtos = new PageImpl<>(new ArrayList<>());
        List<MessageDto> lst = new ArrayList<>();
        for (int i = 0; i < page.getTotalElements(); i++) {
            MessageDto messageDto = page.getContent().get(i);
            Set<User> subscribers = messageDto.getAuthor().getSubscribers();
            ArrayList<User> users = new ArrayList<>(subscribers);
            String username = "";
            for (int j = 0; j < users.size(); j++) {
                if (user.getUsername().equals(users.get(j).getUsername())) {
                    System.out.println(username);
                    lst.add(page.getContent().get(i));
                }
            }
        }
        messageDtos = new PageImpl<>(lst);
        model.addAttribute("page", messageDtos);
        model.addAttribute("url", "/");
        return "home";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        Page<MessageDto> page = messageService.messageList(pageable, filter, user);
        List<Comment> list = commentService.findAll();
        model.addAttribute("comment", list);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model
    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);
        } else {
            saveFile(file, message);

            model.addAttribute("message", null);

            messageRepository.save(message);
        }
        Iterable<Message> messages = messageRepository.findAll();

        model.addAttribute("messages", messages);

        return "redirect:/user-messages/" + user.getId();
    }

    private void saveFile(MultipartFile file, Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "" + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable

    ) {
        Page<MessageDto> page = messageService.messageListForUser(pageable, currentUser, author);
        List<Comment> list = commentService.findAll();
        model.addAttribute("comment", list);
        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url", "/user-messages/" + author.getId());

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("user") Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }

            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }

            saveFile(file, message);

            messageRepository.save(message);
        }

        return "redirect:/user-messages/" + user;
    }

    @GetMapping(("/messages/{messages}/like"))
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("messages") Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = message.getLikes();
        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }
}