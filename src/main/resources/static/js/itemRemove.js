document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete-item');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            const itemId = this.getAttribute('data-id');

            if (confirm('정말로 삭제하시겠습니까?')) {
                const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                const headers = {
                    'Content-Type' : 'application/json; charset=UTF-8'
                };
                headers[csrfHeader] = csrfToken;

                fetch(`/itemInfo/delete?id=${itemId}`, {
                    method: 'DELETE',
                    headers: headers
                })
                .then(response => {
                    return response.text().then(message => {
                        return { status: response.status, message };
                    });
                })
                .then(({ status, message}) => {
                    if (status === 401) {
                        alert(message);
                        if (confirm('로그인 하시겠습니까?')) {
                            window.location.href = '/login';
                        }

                    } else if (status === 403) {
                        alert(message);

                    } else if (status === 404) {
                        alert(message);

                    } else if (status === 200) {
                        alert(message);
                        window.location.reload();

                    } else {
                        throw new Error(message);
                    }
                })
                .catch(error => {
                    alert(error.message);
                });
            }
        });
    });
});