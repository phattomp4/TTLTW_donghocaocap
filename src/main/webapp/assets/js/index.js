/* phan script cua Phat -- > */

const btnScrollToTop = document.getElementById("btnScrollToTop");

window.onscroll = function () {
    scrollFunction();
};

function scrollFunction() {
    // Nếu cuộn xuống quá 200px thì hiện nút
    if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
        btnScrollToTop.classList.add("show");
    } else {
        // Nếu ở trên đầu trang thì ẩn nút
        btnScrollToTop.classList.remove("show");
    }
}

// Hàm xử lý khi click vào nút
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: "smooth" // ạo hiệu ứng trượt
    });
}


const dotsContainer = document.getElementsByClassName("dot");
if (dotsContainer.length > 0) {

    let slideIndex = 1;
    showSlides(slideIndex);

    function plusSlides(n) {
        showSlides(slideIndex += n);
    }

    function currentSlide(n) {
        showSlides(slideIndex = n);
    }

    function showSlides(n) {
        let i;
        let slides = document.getElementsByClassName("mySlides");
        let dots = document.getElementsByClassName("dot");
        if (n > slides.length) {
            slideIndex = 1
        }
        if (n < 1) {
            slideIndex = slides.length
        }
        for (i = 0; i < slides.length; i++) {
            slides[i].style.display = "none";
        }
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" active", "");
        }
        slides[slideIndex - 1].style.display = "block";
        dots[slideIndex - 1].className += " active";
    }
}


// SWIPERJS
if (document.querySelector('.card-wrapper')) {

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
            0: {
                slidesPerView: 1,
            },
            690: {
                slidesPerView: 2,
            },
            900: {
                slidesPerView: 3,
            },
            950: {
                slidesPerView: 4,
            },
        }
    });
}


// NÚT PROFILE
const profileBtn = document.getElementById('profile-btn');
const profileMenu = document.getElementById('profile-menu');

if (profileBtn && profileMenu) {

    profileBtn.addEventListener('click', () => {

        profileMenu.classList.toggle('show');
    });


    window.addEventListener('click', (event) => {


        if (!profileMenu.contains(event.target) && !profileBtn.contains(event.target)) {
            profileMenu.classList.remove('show');
        }
    });
}


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


if (tabFeatured) {
    tabFeatured.addEventListener('click', () => {
        switchTab(tabFeatured, contentFeatured);
    });
}

if (tabMen) {
    tabMen.addEventListener('click', () => {
        switchTab(tabMen, contentMen);
    });
}

if (tabWomen) {
    tabWomen.addEventListener('click', () => {
        switchTab(tabWomen, contentWomen);
    });
}


/**
 * @param {number} direction
 */
let scrollSlider = (direction) => {
    const slider = document.getElementById("slide-list-img-new");
    if (!slider) {
        console.error("Slider element with ID slide-list-img-new not found.");
        return;
    }

    const firstImage = slider.querySelector('.gallery-image');
    let scrollStep = 300; // Giá trị mặc định an toàn

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
    var txtSearch = param.value;
    var resultBox = document.getElementById("search-results");

    // Nếu ô tìm kiếm trống thì ẩn hộp gợi ý đi
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
        .catch(error => console.error('Lỗi:', error));
}

// Ẩn hộp gợi ý khi click ra ngoài
window.addEventListener('click', function (e) {
    if (!document.querySelector('.search-bar').contains(e.target)) {
        document.getElementById("search-results").style.display = "none";
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

                    btn.parentElement.previousElementSibling.scrollIntoView({behavior: "smooth", block: "start"});
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