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
  fetch('/api/dashboards/create/' + wsid, {
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

const deleteBtns = document.querySelectorAll('.delete-button');
deleteBtns.forEach(function(deleteBtn){
    deleteBtn.addEventListener("click", function() {
      var id = deleteBtn.getAttribute("data-board-id")

      if (!confirm("Are you sure to remove this board?"))
        return;

      const url = `/api/dashboards/delete/${id}`;
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

const renameBtns = document.querySelectorAll(".rename-button");
renameBtns.forEach(function(renameBtn){
    renameBtn.addEventListener("click", function() {
        var newName = prompt("Please, enter new name for this dashboard", "");
        if (newName == null)
            return;

        var boardId = renameBtn.getAttribute("data-board-id");

        var params = new URLSearchParams({
                    name: newName,
                    board: boardId
                });
        fetch("/api/dashboards/rename?" + params, {
              method: "PUT",
              headers: {
                  "Content-Type": "application/json",
              }
          })
        .then(response => {
           if (response.ok)
               location.reload();
        });
    });
})