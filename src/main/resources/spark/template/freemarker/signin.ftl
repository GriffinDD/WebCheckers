<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
    <h1>${title}</h1>
    
    <div class="body">
    
      <h2>Player Login</h2>
      <p>
        ${message1}
        <#include "message.ftl" />
      </p>

    <form action="./signin" method="POST">
            <br/>
            <input name="playerName" />
            <br/><br/>
            <button type="submit">${login}</button>
          </form>
    </div>
  </div>
</body>
</html>
