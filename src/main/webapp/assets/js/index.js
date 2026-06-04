/* phan script cua Phat -- > */

const btnScrollToTop = document.getElementById("btnScrollToTop");

window.onscroll = function() { scrollFunction() };

function scrollFunction() {
    let myElement = document.getElementById("ten_the_cua_ban");
    if (myElement) {
        if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
            myElement.classList.add("scrolled");
        } else {
            myElement.classList.remove("scrolled");
        }
    }
}

// Hàm xử lý khi click vào nút
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: "smooth" // ạo hiệu ứng trượt
    });
}
window.scrollToTop = scrollToTop;


const dotsContainer = document.getElementsByClassName("dot");
if (dotsContainer.length > 0) {
    let slideIndex = 1;
    showSlides(slideIndex);

    function plusSlides(n) { showSlides(slideIndex += n); }
    function currentSlide(n) { showSlides(slideIndex = n); }

    function showSlides(n) {
        let i;
        let slides = document.getElementsByClassName("mySlides");
        let dots = document.getElementsByClassName("dot");

        if (!slides || slides.length === 0) return;

        if (n > slides.length) { slideIndex = 1 }
        if (n < 1) { slideIndex = slides.length }

        for (i = 0; i < slides.length; i++) {
            if(slides[i]) slides[i].style.display = "none";
        }
        for (i = 0; i < dots.length; i++) {
            if(dots[i]) dots[i].className = dots[i].className.replace(" active", "");
        }
        if(slides[slideIndex - 1]) slides[slideIndex - 1].style.display = "block";
        if(dots[slideIndex - 1]) dots[slideIndex - 1].className += " active";
    }

    window.plusSlides = plusSlides;
    window.currentSlide = currentSlide;
}

if (document.querySelector('.card-wrapper') && typeof Swiper !== 'undefined') {
    new Swiper('.card-wrapper', {
        loop: true,
        spaceBetween: 0,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
            dynamicBullets: true,
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        breakpoints: {
            0: { slidesPerView: 1 },
            690: { slidesPerView: 2 },
            900: { slidesPerView: 3 },
            950: { slidesPerView: 4 },
        }
    });
}


// // NÚT PROFILE
// const profileBtn = document.getElementById('profile-btn');
// const profileMenu = document.getElementById('profile-menu');
//
// if (profileBtn && profileMenu) {
//
//     profileBtn.addEventListener('click', () => {
//
//         profileMenu.classList.toggle('show');
//     });
//
//
//     window.addEventListener('click', (event) => {
//
//
//         if (!profileMenu.contains(event.target) && !profileBtn.contains(event.target)) {
//             profileMenu.classList.remove('show');
//         }
//     });
// }


//TAB SẢN PHẨM

const tabFeatured = document.getElementById('tab-featured');
const tabMen = document.getElementById('tab-men');
const tabWomen = document.getElementById('tab-women');


const contentFeatured = document.getElementById('content-featured');
const contentMen = document.getElementById('content-men');
const contentWomen = document.getElementById('content-women');

// Gom các nút và nội dung vào mảng để quản lý
const tabs = [tabFeatured, tabMen, tabWomen];
const contents = [contentFeatured, contentMen, contentWomen];

function switchTab(clickedTab, contentToShow) {
    tabs.forEach(tab => {
        if (tab) tab.classList.remove('active');
    });
    contents.forEach(content => {
        if (content) content.classList.add('hidden');
    });
    if (clickedTab) clickedTab.classList.add('active');

    if (contentToShow) contentToShow.classList.remove('hidden');
}

if (tabFeatured) tabFeatured.addEventListener('click', () => switchTab(tabFeatured, contentFeatured));
if (tabMen) tabMen.addEventListener('click', () => switchTab(tabMen, contentMen));
if (tabWomen) tabWomen.addEventListener('click', () => switchTab(tabWomen, contentWomen));


/**
 * @param {number} direction
 */
let scrollSlider = (direction) => {
    const slider = document.getElementById("slide-list-img-new");
    if (!slider) return;

    const firstImage = slider.querySelector('.gallery-image');
    let scrollStep = 300;

    if (firstImage) {
        const imageWidth = firstImage.offsetWidth;
        const style = window.getComputedStyle(firstImage);
        const marginRight = parseFloat(style.marginRight) || 0;
        scrollStep = imageWidth + marginRight;
    }

    slider.scrollBy({
        left: direction * scrollStep,
        behavior: "smooth"
    });
};
window.scrollSlider = scrollSlider;

function searchByName(param) {
    if (!param) return;
    var txtSearch = param.value;
    var resultBox = document.getElementById("search-results");


    if (!resultBox) return;
    if (txtSearch.trim() === "") {
        resultBox.style.display = "none";
        return;
    }

    // Gọi AJAX bằng Fetch API
    fetch("ajax-search?txt=" + txtSearch)
        .then(response => response.text())
        .then(data => {
            resultBox.innerHTML = data;
            resultBox.style.display = "block";
        })
        .catch(error => console.error('Lỗi tìm kiếm:', error));
}
window.searchByName = searchByName;

// Ẩn hộp gợi ý khi click ra ngoài
window.addEventListener('click', function (e) {
    const searchBar = document.querySelector('.search-bar');
    const resultBox = document.getElementById("search-results");

    // Chỉ thực hiện nếu thẻ searchBar và resultBox tồn tại trên trang
    if (searchBar && resultBox) {
        if (!searchBar.contains(e.target)) {
            resultBox.style.display = "none";
        }
    }
});


document.addEventListener("DOMContentLoaded", function() {

    // Hàm cài đặt chức năng Xem thêm cho từng nhóm
    function setupLoadMore(itemClass, btnId) {
        const itemsPerPage = 8;
        const items = document.querySelectorAll('.' + itemClass);
        const btn = document.getElementById(btnId);

        // ít sản phẩm thì ẩn nút
        if (items.length <= itemsPerPage) {
            if(btn) btn.style.display = 'none';
            return;
        }

        // Ẩn các sản phẩm từ số 9 trở đi
        for (let i = itemsPerPage; i < items.length; i++) {
            items[i].classList.add('hidden-style');
        }

        if(btn) {
            btn.addEventListener('click', function() {
                const hiddenItems = document.querySelectorAll('.' + itemClass + '.hidden-style');

                if (btn.innerText.includes("Xem thêm")) {
                    let count = 0;
                    for (let i = 0; i < hiddenItems.length; i++) {
                        if (count < itemsPerPage) {
                            hiddenItems[i].classList.remove('hidden-style');
                            count++;
                        }
                    }
                    const remaining = document.querySelectorAll('.' + itemClass + '.hidden-style');
                    if (remaining.length === 0) {
                        btn.innerHTML = 'Ẩn bớt <i class="fa-solid fa-chevron-up"></i>';
                    }
                } else {

                    for (let i = itemsPerPage; i < items.length; i++) {
                        items[i].classList.add('hidden-style');
                    }
                    if(btn.parentElement.previousElementSibling) {
                        btn.parentElement.previousElementSibling.scrollIntoView({behavior: "smooth", block: "start"});
                    }
                    btn.innerHTML = 'Xem thêm <i class="fa-solid fa-chevron-down"></i>';
                }
            });
        }
    }

    setupLoadMore('js-item-featured', 'btnNewLoadMore');  // Tab Nổi bật
    setupLoadMore('js-item-men', 'btnLoadMoreMen');       // Tab Nam
    setupLoadMore('js-item-women', 'btnLoadMoreWomen');   // Tab Nữ
});


// SLIDER THƯƠNG HIỆU
function scrollBrand(direction) {
    const container = document.getElementById('brand-slider');
    if (container) {
        const scrollAmount = container.clientWidth / 1.5;
        container.scrollBy({
            left: direction * scrollAmount,
            behavior: 'smooth'
        });
    }
}
window.scrollBrand = scrollBrand;