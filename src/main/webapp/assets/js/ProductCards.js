/* phan script cua Vu */
const page2 = document.getElementById('Page2');
const viewMoreBtn = document.getElementById('view-more-btn');

if (viewMoreBtn && page2) {
    viewMoreBtn.addEventListener('click', () => {
        if (page2.classList.contains('hidden')) {
            page2.classList.remove('hidden');
            viewMoreBtn.textContent = 'Thu gọn';
        } else {
            page2.classList.add('hidden');
            viewMoreBtn.textContent = 'Xem thêm sản phẩm';
            window.scrollTo({
                top: document.querySelector('.Outstanding-clocks').offsetTop - 20,
                behavior: 'smooth'
            });
        }
    });
}
