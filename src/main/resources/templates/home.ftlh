<#import "parts/common.ftlh" as c>

<@c.page>
<h5>Posts of subscriptions</h5>
    <#include "parts/security.ftlh">
    <#import "parts/pager.ftlh" as p>

    <div id="collapses" style="display: none">
        <#include "parts/fill.ftlh"/>
    </div>
    <div class="container card-row" id="message-list">
        <#list page.content as message>
            <div class="card border-dark my-3 p-3" style="width: 30rem;" data-id="${message.id}">
                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top"/>
                </#if>
                <div class="m-2">
                    <span>${message.text}</span><br/>
                    <i>#${message.tag}</i>
                </div>
                <div class="card-footer text-muted container">
                    <div class="row">
                        <a class="col align-self-center"
                           href="/user-messages/${message.author.id}"> ${message.authorName}</a>
                        <a class="col align-self-center" href="/messages/${message.id}/like">
                            <#if message.meLiked>
                                <i class="fas fa-heart"></i>
                            <#else>
                                <i class="far fa-heart"></i>
                            </#if>
                            ${message.likes}
                        </a>
                        <#if message.author.id == currentUserId>
                            <a class="col m-3 btn btn-primary" onclick="bolean()"
                               href="/user-messages/${message.author.id}?message=${message.id}">Edit</a>
                        </#if>
                        <button class="col m-3 btn btn-primary" data-toggle="collapse" onclick="aa(`collap`+${message.id})">Comment</button>

                    </div>
                    <div id="collap${message.id}" style="display: none">
                        <#list comment as comment>
                            <#if message.id == comment.message.id>
                                <p><a class="col align-self-center"
                                      href="/user-messages/${comment.author.id}"> ${comment.author.username}</a>:
                                    ${comment.text}
                                </p>
                            </#if>
                        </#list>
                        <#include "parts/comment.ftlh"/>
                    </div>
                </div>
            </div>
        <#else>
            No messages
        </#list>
    </div>

</@c.page>