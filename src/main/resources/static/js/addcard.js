
function openModal(columnId) {
  var modal = document.getElementById('addCardModal');
  var columnIdField = document.getElementById('columnId');
  columnIdField.value = columnId;
  modal.style.display = 'block';
}

// Close modal
function closeModal() {
  var modal = document.getElementById('addCardModal');
  var form = document.getElementById('addCardForm');
  form.reset();
  modal.style.display = 'none';
}

function submitForm() {
  var title = document.getElementById("title").value;
  var description = document.getElementById("description").value;
  var user = document.getElementById("user").value;
  var boardId = document.querySelector(".board").getAttribute("data-board-id");
  var deadline = document.getElementById("deadline").value;

  var data = {
    title: title,
    description: description,
    assignedUser: user,
    boardId: boardId, // Передача идентификатора доски
    deadline: deadline
  };

  // Отправка POST-запроса на сервер
  fetch("/cards/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
    .then(response => response.json())
    .then(data => {
      // Обработка ответа сервера
      console.log(data);
      // Очистка полей формы и закрытие модального окна
      document.getElementById("titleInput").value = "";
      document.getElementById("descriptionInput").value = "";
      document.getElementById("userInput").value = "";
      document.getElementById("myModal").style.display = "none";
      // Обновление карточек на странице
      loadBoardData();
    })
    .catch(error => {
      console.error("Error:", error);
    });
}
