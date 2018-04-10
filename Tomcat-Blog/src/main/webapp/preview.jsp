<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Preview Post</title>
</head>
<body>
  <div>
      <button  type="submit" name="ClosePreview" onclick="history.back()">Close Preview</button>
  </div>

  <h1><%= request.getAttribute("title") %></h1>
  <%= request.getAttribute("body") %>
</body>
</html>
