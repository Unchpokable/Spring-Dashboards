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