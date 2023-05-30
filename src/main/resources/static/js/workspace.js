document.getElementById('createWorkspaceForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Prevent form submission

  // Get the workspace name from the input field
  var workspaceName = document.getElementById('workspaceName').value;

  // Send a POST request to the server to create a new workspace
  fetch('/api/workspaces/create', {
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

//document.addEventListener('DOMContentLoaded', function() {
//  var workspaces = document.querySelectorAll('.workspace');
//
//  workspaces.forEach(function(workspace) {
//    workspace.addEventListener('click', function() {
//      var id = workspace.getAttribute('data-workspace-id');
//      window.location.href = '/workspaces/workspace/' + id;
//    });
//  });
//});

const deleteBtns = document.querySelectorAll('.delete-button');
deleteBtns.forEach(function(deleteBtn){
    deleteBtn.addEventListener("click", function() {
      var id = deleteBtn.getAttribute("data-workspace-id")

      if (!confirm("Are you sure to remove this workspace? This action will also remove all boards in this workspace"))
        return;

      const url = `/api/workspaces/delete/${id}`;
      const request = new Request(url, { method: "DELETE" });

      fetch(request)
        .then(response => {
          if (response.ok) {
            location.reload();
            console.log('workspace removed');
          } else {
            // Действия при ошибке удаления
          }
        })
        .catch(error => {
          // Действия при ошибке запроса
        });
    });
});
