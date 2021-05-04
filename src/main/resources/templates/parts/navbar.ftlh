<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Switter</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/home">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/main">Recomendation</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/user-messages/${currentUserId}">Profile</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User list</a>
                </li>
            </#if>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Settings</a>
                </li>
            </#if>
        </ul>
        <#if user??>
            <div class="mr-5">
                <form method="get" action="/main" class="form-inline">
                    <input type="text" name="filter" class="form-control" value="${filter?ifExists}"
                           placeholder="search by tag">
                    <button type="submit" class="btn btn-primary ml-2">Search</button>
                </form>
            </div>
        </#if>

        <div class="navbar-text mr-3 ml-3"><#if user??>${name}<#else>Please, login</#if></div>
        <@l.logout />
    </div>
</nav>