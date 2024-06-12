document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete-item');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            const itemId = this.getAttribute('data-id');

            if (confirm('정말로 삭제하시겠습니까?')) {
                fetch(`/itemInfo/delete?id=${itemId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (response.status === 401) {
                        alert('로그인이 필요한 기능입니다');

                        if (confirm('로그인 하시겠습니까?')) {
                            window.location.href = '/login';
                        }

                        return;
                    } else if (response.status === 403) {
                        alert('삭제 권한이 없습니다.');
                        return;
                    } else if (response.status === 404) {
                        alert('상품을 찾을 수 없습니다.');
                        return;
                    } else if (!response.ok) {
                        throw new Error('삭제 중 오류가 발생했습니다');
                    }

                    return response.text();
                })
                .then(message => {
                    if (message) {
                        alert(message);
                        window.location.reload();
                    }
                })
                .catch(error => {
                    console.error('Error : ', error);
                    alert('삭제 중 오류가 발생했습니다');
                })
            }
        });
    });
});