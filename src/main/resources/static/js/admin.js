$(document).ready(function() {
    // Load user data on page load
    getLoggedInUser();
    getUsers();
    getRoles();

    // Handle user form submission
    $('#addUserForm').submit(function(event) {
        event.preventDefault();
        addUser($(this).serialize());
    });

    // Handle logout form submission
    $('#logoutForm').submit(function(event) {
        event.preventDefault();
        logout();
    });
});

function getLoggedInUser() {
    fetch('/api/user/currentUser')
        .then(response => response.json())
        .then(user => {
            $('#activeUserName').text(user.name);
            $('#activeUserRoles').text(user.roles.map(role => role.name).join(', '));
        });
}


function getUsers() {
    fetch('/api/admin/userList')
        .then(response => response.json())
        .then(users => {
            $('#usersTableBody').empty();
            users.forEach(user => {
                $('#usersTableBody').append(`
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${user.roles.map(role => role.name).join(', ')}</td>
                        <td>
                            <button class="btn btn-primary" onclick="showModalEdit(${user.id})">Edit</button>
                            <button class="btn btn-danger" onclick="showModalDelete(${user.id})">Delete</button>
                        </td>
                    </tr>
                `);
            });
        });
}
function showModalEdit(userId) {
    fetch(`/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            $('#editUserId').val(user.id);
            $('#editUserName').val(user.name);
            $('#editUserLastName').val(user.lastName);
            $('#editUserAge').val(user.age);
            $('#editUserEmail').val(user.email);
            $('#editUserButton').attr('onclick', 'saveChanges()');


            // Получаем список ролей пользователя
            const userRoles = user.roles;

            // Получаем список всех опций в select
            const roleSelect = document.getElementById('editUserRoles');
            const options = roleSelect.options;

            // Сбрасываем выбранные значения
            for (let i = 0; i < options.length; i++) {
                options[i].selected = false;
            }

            // Устанавливаем выбранные значения для ролей пользователя
            userRoles.forEach(userRole => {
                for (let i = 0; i < options.length; i++) {
                    if (options[i].value === userRole.name) {
                        options[i].selected = true;
                        break;
                    }
                }
            });


            $('#editModal').modal('show');
        });
}

function saveChanges() {
    const id = $('#editUserId').val();
    const name = $('#editUserName').val();
    const lastName = $('#editUserLastName').val();
    const age = $('#editUserAge').val();
    const email = $('#editUserEmail').val();
    const password = $('#editUserPassword').val();
    const roles = $('#editUserRoles').val();

    const link = '#errorAlertModal';
    switch ('') {
        case name:
            $(link).text('Please fill name.');
            $(link).removeClass('d-none');
            return;
        case lastName:
            $(link).text('Please fill last name.');
            $(link).removeClass('d-none');
            return;
        case age:
            $(link).text('Please fill age.');
            $(link).removeClass('d-none');
            return;
        case email:
            $(link).text('Please fill email.');
            $(link).removeClass('d-none');
            return;
        case password:
            $(link).text('Please fill password.');
            $(link).removeClass('d-none');
            return;
    }

    if (roles.length === 0) {
        console.info("")
        $(link).text('Please choose at least one role.');
        $(link).removeClass('d-none');
        return;
    }

    const user = {
        id: id,
        name: name,
        lastName: lastName,
        age: parseInt(age),
        email: email,
        password: password,
        roles: roles.map(roleName => ({ name: roleName, id: $('#rolesAdd option[value="' + roleName + '"]').attr('data-id') }))
    };

    fetch(`/api/admin/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(response => {
        if (response.ok) {
            console.info('successfully saved user :' + id)
            // Обновляем таблицу после успешного сохранения
            getUsers();
            $('#errorAlertModal').addClass('d-none');
            $('#editModal').modal('hide'); // Закрываем модальное окно
        } else {
            $('#errorAlertModal').removeClass('d-none');
        }
    });
}


function showModalDelete(userId) {
    fetch(`/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            $('#deleteUserId').val(user.id);
            $('#deleteUserName').val(user.name);
            $('#deleteUserLastName').val(user.lastName);
            $('#deleteUserAge').val(user.age);
            $('#deleteUserEmail').val(user.email);
            $('#deleteUserButton').attr('onclick', `deleteUser(${user.id})`);

            // Получаем список ролей пользователя
            const userRoles = user.roles;

            // Получаем список всех опций в select
            const roleSelect = document.getElementById('deleteUserRoles');
            const options = roleSelect.options;

            // Сбрасываем выбранные значения
            for (let i = 0; i < options.length; i++) {
                options[i].selected = false;
            }

            // Устанавливаем выбранные значения для ролей пользователя
            userRoles.forEach(userRole => {
                for (let i = 0; i < options.length; i++) {
                    if (options[i].value === userRole.name) {
                        options[i].selected = true;
                        break;
                    }
                }
            });


            $('#deleteModal').modal('show');
        });
}

function deleteUser(id) {
    console.info('deleting user :' + id)
        fetch(`/api/admin/${id}`, {
            method: 'DELETE',
        }).then(response => {
            if (response.ok) {
                // Обновляем таблицу после успешного удаления
                getUsers();
                $('#deleteModal').modal('hide'); // Закрываем модальное окно
            } else {
                console.error('Failed to delete user');
            }
        });
}



function getRoles() {
    fetch('/api/admin/roleList')
        .then(response => response.json())
        .then(roles => {
            let select = $('#rolesAdd');
            select.empty();
            roles.forEach(role => {
                select.append(`<option value="${role.name}" data-id="${role.id}">${role.name}</option>`);
            });

            select = $('#editUserRoles');
            select.empty();
            roles.forEach(role => {
                select.append(`<option value="${role.name}" data-id="${role.id}">${role.name}</option>`);
            });

            select = $('#deleteUserRoles');
            select.empty();
            roles.forEach(role => {
                select.append(`<option value="${role.name}" data-id="${role.id}">${role.name}</option>`);
            });
        });
}



function addUser() {
    const name = $('#name').val();
    const lastName = $('#lastName').val();
    const age = $('#age').val();
    const email = $('#email').val();
    const password = $('#password').val();
    const roles = $('#rolesAdd').val();

    const link = '#errorAlert';
    switch ('') {
        case name:
            $(link).text('Please fill name.');
            $(link).removeClass('d-none');
            return;
        case lastName:
            $(link).text('Please fill last name.');
            $(link).removeClass('d-none');
            return;
        case age:
            $(link).text('Please fill age.');
            $(link).removeClass('d-none');
            return;
        case email:
            $(link).text('Please fill email.');
            $(link).removeClass('d-none');
            return;
        case password:
            $(link).text('Please fill password.');
            $(link).removeClass('d-none');
            return;
    }

    if (roles.length === 0) {
        console.info("")
        $(link).text('Please choose at least one role.');
        $(link).removeClass('d-none');
        return;
    }

    const userData = {
        name: name,
        lastName: lastName,
        age: parseInt(age),
        email: email,
        password: password,
        roles: roles.map(roleName => ({ name: roleName, id: $('#rolesAdd option[value="' + roleName + '"]').attr('data-id') }))
    };

    fetch('/api/admin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
        .then(response => {
            if (response.ok) {
                console.info('successfully added user :' + id)
                getUsers();
                document.getElementById('users-tab').click();
                $('#errorAlert').addClass('d-none');
            } else {
                $('#errorAlert').text('Email is already taken. Please choose another one.');
                $('#errorAlert').removeClass('d-none');
            }
        });
}

function logout() {
    fetch('/logout', { method: 'POST' })
        .then(() => {
            window.location.replace('/login');
        });
}


