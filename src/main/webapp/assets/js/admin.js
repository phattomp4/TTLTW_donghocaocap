
    document.addEventListener('DOMContentLoaded', function () {
            // Lấy tất cả các link menu (trả về một danh sách mảng)
            const navLinks = document.querySelectorAll('.nav-link');
            // Lấy tất cả các trang (trả về một danh sách mảng)
            const pages = document.querySelectorAll('.page-content');

    // Lấy các nút bấm bằng ID
    const btnAddProduct = document.getElementById('btn-add-product');
    const btnCancelProduct = document.getElementById('btn-cancel-product');

            /**
             * Hàm "showPage" - điều hướng.
             * Ẩn tất cả các trang, sau đó chỉ hiện trang có ID được truyền vào.
             * @param {string} pageId - ID của trang cần hiển thị (ví dụ: 'page-products')
    */
    function showPage(pageId) {
        pages.forEach(page => {
            // Xóa class active ở tất cả các trang
            page.classList.remove('active');
        });
                navLinks.forEach(link => {
        link.classList.remove('active');
                });

    // Hiển thị trang được chọn
    const activePage = document.getElementById(pageId);
    if (activePage) { // Kiểm tra trang có tồn tại ko
        activePage.classList.add('active'); // Thêm active (CSS làm nó hiện ra)
                }

    const activeLink = document.querySelector(`.nav-link[data-page="${pageId}"]`);
    if (activeLink) {
        activeLink.classList.add('active');
    S
                }
            }
            navLinks.forEach(link => {
        link.addEventListener('click', function (e) {

            const pageId = this.getAttribute('data-page');
            if (pageId) {
                showPage(pageId);
            }
        });
            });

    // Sự kiện cho nút Thêm sản phẩm mới
    if (btnAddProduct) {
        btnAddProduct.addEventListener('click', function () {
            showPage('page-add-product');

            // Đổi tiêu đề và xóa trắng form
            document.getElementById('form-title').innerText = 'Thêm sản phẩm mới';
            document.getElementById('product-form').reset();
        });
            }

    // Sự kiện cho nút Hủy"
    if (btnCancelProduct) {
        btnCancelProduct.addEventListener('click', function () {
            showPage('page-products');
        });
            }

            // Sự kiện cho các nút Sửa sản phẩm
            document.querySelectorAll('#page-products .btn-icon.edit').forEach(button => {
        button.addEventListener('click', function () {
            showPage('page-add-product');
            // Đổi tiêu đề
            document.getElementById('form-title').innerText = 'Chỉnh sửa sản phẩm';
            document.getElementById('product-name').value = 'Tên sản phẩm được sửa';
            document.getElementById('product-sku').value = 'SKU-EDIT';
            document.getElementById('product-price').value = 123456;
        });
            });

            // Sự kiện cho nút Xóa
            document.querySelectorAll('.btn-icon.delete').forEach(button => {
        button.addEventListener('click', function () {
            if (confirm('Bạn có chắc chắn muốn xóa mục này?')) {
                alert('Đã gửi yêu cầu xóa! (Demo)');
            }
        });
            });

        });
