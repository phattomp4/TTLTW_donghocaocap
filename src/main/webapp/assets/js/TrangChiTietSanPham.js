
document.addEventListener('DOMContentLoaded', function() {

    const mainImage = document.getElementById('mainImage');
    const thumbnailList = document.querySelector('.Thumbnail-list');
    const scrollLeftBtn = document.getElementById('scrollLeft');
    const scrollRightBtn = document.getElementById('scrollRight');
    const thumbnails = document.querySelectorAll('.thumbnail');

    const scrollAmount = 140; 

    thumbnails.forEach(thumbnail => {
        thumbnail.addEventListener('click', function() {
            const newSrc = this.getAttribute('data-full-src');
            mainImage.src = newSrc;
            thumbnails.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
        });
    });

    function scrollThumbnails(direction) {
        if (direction === 'left') {
            thumbnailList.scrollBy({
                left: -scrollAmount,
                behavior: 'smooth'
            });
        } else if (direction === 'right') {
            thumbnailList.scrollBy({
                left: scrollAmount,
                behavior: 'smooth'
            });
        }
    }

    scrollLeftBtn.addEventListener('click', () => scrollThumbnails('left'));
    scrollRightBtn.addEventListener('click', () => scrollThumbnails('right'));
});


const form = document.getElementById('them_gio_hang_form');
form.addEventListener('submit', function (event) {

    event.preventDefault();

    if (form.checkValidity()) {
        alert('🎉 Đã thêm vào giỏ hàng!');
    } else {
        form.reportValidity();
    }
});