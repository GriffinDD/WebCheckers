 <div class="navigation">
  <#if currentUser??>
    <a href="/">my home</a> |
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">sign out [${currentUser.name}]</a>
    </form>
  <#else>
    <a href="/signin">sign in</a>
  </#if>
  | <a href="/themes">Choose themes</a>
  | <a href="/help">Player Help</a>
 </div>
