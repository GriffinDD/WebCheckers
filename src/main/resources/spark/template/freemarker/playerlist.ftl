<head>
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<form action="./game" method="POST">
        <br/>
        <input name="OpposingPlayerName" />
        <br/><br/>
    <button type="submit">${challenge}</button>
    </form>
<div class="playerlist">
  <meta http-equiv="refresh" content="10">

  <#if currentUser??>
    ${playerlobby.getListOfPlayers(currentUser.name)}
  <#else>
    <p> Sign-in to see players </p>
  </#if>
</div>
<b>
<#if playerlobby??>     
    <br>
    Player Count: ${playerlobby.numberOfPlayers}
    </b>
</#if>