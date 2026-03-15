 // Hàm JavaScript để chuyển đổi Tab
        function openTab(evt, contentName) {
            let i, tabcontent, tablinks, titleElement;

            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].style.display = "none";
            }

            tablinks = document.getElementsByClassName("tablinks");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].classList.remove("active");
            }
            document.getElementById(contentName).style.display = "block";
            evt.currentTarget.classList.add("active");

            const newTitle = evt.currentTarget.getAttribute('data-content-title');
            document.getElementById('current-title').textContent = newTitle;
        }

        document.addEventListener('DOMContentLoaded', (event) => {
            document.getElementById("btn-info").click();
        });

        // Hàm xử lý chuyển menu chính
function openTab(evt, contentName) {
    let i, tabcontent, tablinks;

    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("active");
    }

    document.getElementById(contentName).style.display = "block";

    evt.currentTarget.classList.add("active");

    const newTitle = evt.currentTarget.getAttribute('data-content-title');
    document.getElementById('current-title').textContent = newTitle;

    if (contentName === 'history-content') {
        const defaultOrderTab = document.querySelector('#history-content .order-tab-group .order-tab');
        if (defaultOrderTab) {
            openOrderStatusTab(null, 'all-orders');
            defaultOrderTab.classList.add('active');
        }
    }
}

// Hàm chuyển đổi các tab menu trạng thái đơn hàng
function openOrderStatusTab(evt, contentName) {
    let i, orderContent, orderTabs;

    orderContent = document.getElementsByClassName("order-content");
    for (i = 0; i < orderContent.length; i++) {
        orderContent[i].style.display = "none";
    }

    orderTabs = document.getElementsByClassName("order-tab");
    for (i = 0; i < orderTabs.length; i++) {
        orderTabs[i].classList.remove("active");
    }

    document.getElementById(contentName).style.display = "block";

    if (evt && evt.currentTarget) {
        evt.currentTarget.classList.add("active");
    }
}


// Kích hoạt tab mặc định khi tải trang
document.addEventListener('DOMContentLoaded', (event) => {
    const defaultTab = document.querySelector('.left-side .tablinks');
    if (defaultTab) {
        openTab({currentTarget: defaultTab}, defaultTab.getAttribute('onclick').split("'")[1]);
    }
});