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
    fetch('/api/users/currentUser')
        .then(response => response.json())
        .then(user => {
            $('#activeUserName').text(user.name);
            $('#activeUserRoles').text(user.roles.map(role => role.name).join(', '));
        });
}


function getUsers() {
    fetch('/api/users/userList')
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
                            <button class="btn btn-primary" onclick="editUser(${user.id})">Edit</button>
                            <button class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                        </td>
                    </tr>
                `);
            });
        });
}

function getRoles() {
    fetch('/api/users/roleList')
        .then(response => response.json())
        .then(roles => {
            const select = $('#rolesAdd');
            select.empty();
            roles.forEach(role => {
                select.append(`<option value="${role.name}">${role.name}</option>`);
            });
        });
}


function addUser() {
    const name = $('#name').val();
    const lastName = $('#lastName').val();
    const age = $('#age').val();
    const email = $('#email').val();
    const password = $('#password').val();
    console.info(name, lastName, age, email, password);
    const roles = $('#rolesAdd').val();

    const userData = {
        name: name,
        lastName: lastName,
        age: parseInt(age),
        email: email,
        password: password,
        roles: roles
    };

    fetch('/api/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
        .then(response => {
            if (response.ok) {
                getUsers();
            } else {
                $('#errorAlert').removeClass('d-none');
            }
        });
}




function editUser(userId) {
    // Implement editing user
}

function deleteUser(userId) {
    fetch(`/api/users/${userId}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                getUsers();
            } else {
                console.error('Failed to delete user');
            }
        });
}

function logout() {
    fetch('/logout', { method: 'POST' })
        .then(() => {
            window.location.replace('/login');
        });
}
