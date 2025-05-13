<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="5">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />
  <h2>Players Online: </h2>

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

  </div>
  <#include "playerlist.ftl" />
</div>
</body>

</html>
