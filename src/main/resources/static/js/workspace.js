document.addEventListener('DOMContentLoaded', function() {
    var workspaceLinks = document.querySelectorAll('a[data-workspace-id]');
    workspaceLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            var workspaceId = link.getAttribute('data-workspace-id');
            var requestUrl = '/workspace/' + workspaceId;
            fetch(requestUrl, {
                method: 'GET',
            })
            .then(function(response) {
                if (response.ok) {
                    window.location.href = requestUrl;
                } else {
                    console.error('Error:', response.statusText);
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
            event.preventDefault();
        });
    });
});


document.getElementById('createWorkspaceForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Prevent form submission

  // Get the workspace name from the input field
  var workspaceName = document.getElementById('workspaceName').value;

  // Send a POST request to the server to create a new workspace
  fetch('/workspaces/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(workspaceName)
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
  var workspaces = document.querySelectorAll('.workspace');

  workspaces.forEach(function(workspace) {
    workspace.addEventListener('click', function() {
      var id = workspace.getAttribute('data-workspace-id');
      window.location.href = '/workspaces/workspace/' + id;
    });
  });
});