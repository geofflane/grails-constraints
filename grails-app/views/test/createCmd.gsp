<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title>New Address</title>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLinkTo(action: 'index')}">Home</a></span>
</div>
<div class="body">
  <h1>New Address</h1>

  <g:hasErrors bean="${address}">
    <div class="errors">
      <g:renderErrors bean="${address}" as="list"/>
    </div>
  </g:hasErrors>

  <g:form action="saveCmd" method="post">

    <label for="street">Street:</label>
    <span class="value ${hasErrors(bean: address, field: 'street', 'errors')}">
      <input type="text" id="street" name="street" value="${fieldValue(bean: address, field: 'street')}"/>
    </span>

    <label for="zip">Zip:</label>
    <span class="value ${hasErrors(bean: address, field: 'zip', 'errors')}">
      <input type="text" id="zip" name="zip" value="${fieldValue(bean: address, field: 'zip')}"/>
    </span>

    <label for="phone">phone:</label>
    <span class="value ${hasErrors(bean: address, field: 'phone', 'errors')}">
      <input type="text" id="phone" name="phone" value="${fieldValue(bean: address, field: 'phone')}"/>
    </span>


    <div class="buttons">
      <span class="button"><input class="save" type="submit" value="Create"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
