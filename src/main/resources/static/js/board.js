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