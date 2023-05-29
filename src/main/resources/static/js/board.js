document.addEventListener('DOMContentLoaded', function() {
        var workspaceLinks = document.querySelectorAll('a[data-board-id]');
        workspaceLinks.forEach(function(link) {
            link.addEventListener('click', function(event) {
                var boardId = link.getAttribute('data-board-id');
                var requestUrl = '/boards/' + boardId;
                fetch(requestUrl, {
                    method: 'GET',
                })
                .then(function(response) {
                    if (response.ok) {
                        console.log("request succeed")
                    }
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
                event.preventDefault();
            });
        });
    });

document.getElementById('createBoardForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Prevent form submission

  // Get the workspace name from the input field
  var boardName = document.getElementById('boardName').value;
  var wsid = document.getElementById("createBoardForm").getAttribute("data-workspace-id");
  // Send a POST request to the server to create a new workspace
  fetch('/dashboards/create/' + wsid, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(boardName)
  })
  .then(function(response) {
    if (response.ok) {
      // Workspace created successfully, perform any necessary actions
      console.log('Workspace created');
      location.reload();
    } else {
      // Handle error response
      console.error('Error creating workspace');
    }
  })
  .catch(function(error) {
    console.error('Error creating workspace:', error);
  });
});

document.addEventListener('DOMContentLoaded', function() {
  var workspaces = document.querySelectorAll('.board');

  workspaces.forEach(function(workspace) {
    workspace.addEventListener('click', function() {
      var id = workspace.getAttribute('data-board-id');
      window.location.href = '/dashboards/board/' + id;
    });
  });
});