function submitChanges() {
    var newUsername = document.getElementById("username").value;
    var newEmail = document.getElementById("email").value;
    var newAbout = document.getElementById("about").value;
    var userId = document.getElementById("userProfile").getAttribute("data-user-id");

    var data = {
        nickname: newUsername,
        email: newEmail,
        about: newAbout
    }

    fetch('/api/users/update?userId=' + userId, {
        method: "POST",
        headers: {
              "Content-Type": "application/json"
            },
        body: JSON.stringify(data)
    })
    then(response => {
        if (response.ok)
            location.reload();
    })
}