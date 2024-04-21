$(document).ready(function() {
    getCurrentUser();

    $('#logoutForm').submit(function(event) {
        event.preventDefault();
        logout();
    });

});

function getCurrentUser() {
    console.info('getCurrentUser');
    fetch('/api/user/currentUser')
        .then(response => response.json())
        .then(user => {

            if (!user.roles.map(role => role.name).includes('ROLE_ADMIN')){
                $('#admin-link').hide();
            }

            $('#userName').text(user.name);
            $('#userRoles').text(user.roles.map(role => role.name).join(', '));

            const userTableBody = document.getElementById('userTableBody');
            userTableBody.innerHTML = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => role.name).join(', ')}</td>
                </tr>
            `;

        });
}

function logout() {
    fetch('/logout', { method: 'POST' })
        .then(() => {
            window.location.replace('/login');
        });
}