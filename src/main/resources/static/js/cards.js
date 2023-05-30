document.getElementById("addCardForm").addEventListener("submit", submitForm);

function openModal(columnId) {
  var modal = document.getElementById('addCardModal');
  var columnIdField = document.getElementById('columnId');
  columnIdField.value = columnId;
  modal.style.display = 'block';

  var select = document.getElementById("categorySelect");

  if (columnId == "planned"){
    select.value = "Planned";
  }

  if (columnId == "inProgress") {
    select.value = "In Progress";
  }

  if (columnId == "problem") {
    select.value = "Some Problems";
  }

  if (columnId == "done") {
    select.value = "Done";
  }
}

// Close modal
function closeModal() {
  var modal = document.getElementById('addCardModal');
  var form = document.getElementById('addCardForm');
  form.reset();
  modal.style.display = 'none';
}

function submitForm(event) {
  event.preventDefault();
  var title = document.getElementById("title").value;
  var description = document.getElementById("description").value;
  var user = document.getElementById("user").value;
  var boardId = document.querySelector(".board").getAttribute("data-board-id");
  var deadline = document.getElementById("deadline").value;
  var categorySelect = document.getElementById("categorySelect");
  var category = categorySelect.options[categorySelect.selectedIndex].text.replace(" ", "");

  var data = {
    title: title,
    description: description,
    assignedUser: user,
    dashboardId: boardId,
    deadline: deadline,
    category: category
  };

  // Отправка POST-запроса на сервер
  fetch("/api/cards/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
    .then(response => {
        if (response.status == 418) { // ТИПА ЧАЙНИК, НУ ВЫ ПОНЯЛИ, ДА?)) ЧАЙНИК ТИПА ХПВАХПАВПРОВАРПВАП СЕРВЕР ЧАЙНИК И НЕ ХОЧЕТ РАБОТАТЬ ВАВЫРАПХАХПХАП ВРЕМЯ 7 УТРА ГОСПОДИ СПАСИ Я ЕЩЁ НЕ ЛОЖИЛСЯ ДАЖЕ
            alert("The specified user is not a member of the board or does not exist in the system");
            return;
        }

        if (response.ok) {
            document.getElementById("addCardForm").reset();
            closeModal();
            location.reload();
        }
    })
    .catch(error => {
      console.error("Error:", error);
    });
}

function deleteCard(cardId) {
    if (!confirm("Are you sure to remove this card?"))
        return;

    var board = document.getElementById("boardContainer").getAttribute("data-board-id");
    var params = new URLSearchParams({
        cardId: cardId,
        boardId: board
    });

    fetch("/api/cards/remove?" + params, {
    method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            location.reload();
        }
    });
}

var activeMenu = null;

function clickOutsideMenu(event) {
  if (activeMenu && !activeMenu.contains(event.target)) {
    activeMenu.remove();
    activeMenu = null;
  }
}

function contextMenu(event) {
  event.preventDefault();

  if (activeMenu) {
        activeMenu.remove();
      }


  let cardId = null;
  let currentCategory = null;
  if (event.target.tagName == "DIV") {
      cardId = event.target.getAttribute("data-card-id");
      currentCategory = event.target.parentNode.getAttribute("data-column-type");
  }
  else {
    cardId = event.target.parentNode.getAttribute("data-card-id");
    currentCategory = event.target.parentNode.parentNode.getAttribute("data-column-type");
  }

  // Создаем всплывающее меню
  const menu = document.createElement("div");
  menu.classList.add("menu");
  menu.style.left = event.clientX + "px";
  menu.style.top = event.clientY + "px";

  // Создаем пункты меню для доступных категорий
  const categories = ["Planned", "In Progress", "Some Problems", "Done"];
  categories.forEach((category) => {
    const menuItem = document.createElement("div");
    menuItem.classList.add("menu-item");
    menuItem.textContent = category;
    menuItem.addEventListener("click", function () {
      updateCardCategory(cardId, category.replace(" ", ""));
      menu.remove();
    });
    menu.appendChild(menuItem);
  });

  // Добавляем меню в DOM
  document.body.appendChild(menu);

  activeMenu = menu;
}

// Обновление категории карточки на сервере
function updateCardCategory(cardId, category) {
  const url = `/api/cards/shift?card=${cardId}&category=${category}`;
  // Отправляем запрос на сервер для обновления информации о карточке
  // Используйте fetch или другую библиотеку для отправки запроса
  fetch(url, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    // Дополнительные параметры запроса, если необходимо
  })
    .then((response) => {
      if (response.ok)
        location.reload();
    })
    .catch((error) => {
      // Дополнительные действия при ошибке обновления информации о карточке
    });
}

const cards = document.querySelectorAll(".card");

// Добавляем обработчики событий к карточкам
cards.forEach((card) => {
  card.addEventListener("contextmenu", contextMenu);
});

// Добавляем обработчик события клика на весь документ
document.addEventListener("click", clickOutsideMenu);

function addUserToBoard() {
    var userName = document.getElementById("inviteNickname").value;
    var boardId = document.getElementById("boardContainer").getAttribute("data-board-id");

    var params = new URLSearchParams({
            user: userName,
            board: boardId
        });

    fetch("/api/dashboards/addmember?" + params, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        }
    })
    .then(response => {
        if (response.ok)
            location.reload();
        if (response.status == 404)
            alert("Given nickname does not exists in the system");
    });
}