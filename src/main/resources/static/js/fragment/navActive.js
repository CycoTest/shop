document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-links a');
    const writeLink = document.getElementById('write-link');

    navLinks.forEach(link => {
        const linkPath = link.getAttribute('href')
        if (linkPath === currentPath ||
            (currentPath.includes('/itemInfo/write') && link.id === 'write-link') ||
            (currentPath.includes('/noticeInfo/write') && link.id === 'write-link')) {
            link.classList.add('active')
        } else {
            link.classList.remove('active')
        }
    });

    if (writeLink) {
        writeLink.addEventListener('click', function (e) {
            e.preventDefault();
            const isAuthenticated = document.querySelector('.loginStatus span[sec\\:authorize="isAuthenticated()"]') != null;
            if (isAuthenticated) {
                // 로그인 상태일 때 유저가 현재 접속한 페이지에 따라 이동
                if (currentPath.includes('/notice')) {
                    window.location.href = '/noticeInfo/write';
                } else {
                    window.location.href = 'itemInfo/write';
                }
            } else {
                // 로그인하지 않은 상태일 때
                window.location.href = '/login?message=login_required';
            }
        });

        // Write 페이지에서 Write 링크 활성화
        if (currentPath.includes('/itemInfo/write') || currentPath.includes('/noticeInfo/write')) {
            writeLink.classList.add('active');
        }
    }
});