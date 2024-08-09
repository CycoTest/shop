document.addEventListener('DOMContentLoaded', () => {
    const showCommentBtn = document.getElementById('showCommentBtn');
    const commentContainer = document.getElementById('commentContainer');
    const cancelBtn = document.getElementById('cancelBtn');
    const commentForm = document.querySelector('#commentContainer form');
    const commentContent = document.getElementById('commentContent');

    showCommentBtn.addEventListener('click', () => {
        checkLoginStatus();
    });

    cancelBtn.addEventListener('click', () => {
        resetComment();
    });

    commentForm.addEventListener('submit', (event) => {
        event.preventDefault();
        submitComment();
    });

    function checkLoginStatus() {
        fetch('/api/check-auth', {
            method : 'GET',
            credentials : 'same-origin'
        })
        .then(response => {
            if (response.status === 200) {
                showCommentContainer();
            } else if (response.status === 401) {
                showLoginMsg();
            }
        })
        .catch(error => console.error('Error : ', error));
    }

    function showCommentContainer() {
        showCommentBtn.style.display = 'none';
        commentContainer.style.display = 'block';
    }

    function showLoginMsg() {
        if (confirm('로그인이 필요한 기능입니다.')) {
            if (confirm('로그인 하시겠습니까?')) {
                window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
            }
        }
    }

    function resetComment() {
        commentContent.value = '';
        commentContainer.style.display = 'none';
        showCommentBtn.style.display = 'block';
    }

    function submitComment() {
        const token = document.querySelector('meta[name="_csrf"]').content;
        const header = document.querySelector('meta[name="_csrf_header"]').content;

        const formData = new FormData(commentForm);

        fetch('/comment', {
            method : 'POST',
            headers : {
                [header] : token
            },
            body : formData
        })
        .then(response => {
            if (response.ok) {
                alert('댓글이 등록되었습니다.');
                resetComment();
                // 댓글 목록 새로고침
            } else {
                alert('댓글 등록 중 오류가 발생했습니다.');
            }
        })
        .catch(error => console.error('Error : ', error));
    }
});