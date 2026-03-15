<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link href="css/admin.css" rel="stylesheet">
</head>
<body>

    <nav class="sidebar">
        <div class="sidebar-header">
            <i class="fas fa-clock"></i>
            <span>WatchAdmin</span>
        </div>

        <ul class="sidebar-nav">
            <li>
                <a class="nav-link active" data-page="page-dashboard">
                    <i class="fas fa-tachometer-alt"></i>
                    Bảng điều khiển
                </a>
            </li>
            <li>
                <a class="nav-link" data-page="page-products">
                    <i class="fas fa-box"></i>
                    Quản lý Sản phẩm
                </a>
            </li>
            <li>
                <a class="nav-link" data-page="page-orders">
                    <i class="fas fa-receipt"></i>
                    Quản lý Đơn hàng
                </a>
            </li>
            <li>
                <a class="nav-link" data-page="page-users">
                    <i class="fas fa-users"></i>
                    Quản lý Khách hàng
                </a>
            </li>
            <li>
                <a class="nav-link" data-page="page-settings">
                    <i class="fas fa-cog"></i>
                    Cài đặt
                </a>
            </li>
        </ul>
    </nav>

    <div class="main-wrapper">

        <header class="header">
            <div class="header-profile">
                <i class="fas fa-bell"></i> <span>Chào, Admin!</span>
            </div>
        </header>

        <main class="main-content">

            <div id="page-dashboard" class="page-content active">
                <div class="page-header">
                    <h1>Bảng điều khiển</h1>
                </div>

                <div class="stat-cards">
                    <div class="card sales">
                        <div class="card-icon"><i class="fas fa-dollar-sign"></i></div>
                        <h3>Tổng doanh thu (Tháng)</h3>
                        <p>1.250.000.000 ₫</p>
                    </div>
                    <div class="card orders">
                        <div class="card-icon"><i class="fas fa-receipt"></i></div>
                        <h3>Đơn hàng mới</h3>
                        <p>120</p>
                    </div>
                    <div class="card products">
                        <div class="card-icon"><i class="fas fa-box"></i></div>
                        <h3>Tổng sản phẩm</h3>
                        <p>1.850</p>
                    </div>
                    <div class="card customers">
                        <div class="card-icon"><i class="fas fa-users"></i></div>
                        <h3>Khách hàng mới</h3>
                        <p>85</p>
                    </div>
                </div>

                <div class="data-table">
                    <h2>Đơn hàng mới nhất</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Mã Đơn</th>
                                <th>Khách hàng</th>
                                <th>Ngày đặt</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>#1256</td>
                                <td>Nguyễn Văn A</td>
                                <td>11/11/2025</td>
                                <td>12.457.000 ₫</td>
                                <td><span class="status pending">Chờ xử lý</span></td>
                            </tr>
                            <tr>
                                <td>#1255</td>
                                <td>Trần Thị B</td>
                                <td>10/11/2025</td>
                                <td>3.801.000 ₫</td>
                                <td><span class="status completed">Đã giao</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div id="page-products" class="page-content">
                <div class="page-header">
                    <h1>Quản lý Sản phẩm</h1>
                    <button class="btn btn-primary" id="btn-add-product">
                        <i class="fas fa-plus"></i> Thêm sản phẩm mới
                    </button>
                </div>

                <div class="data-table">
                    <h2>Danh sách sản phẩm</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Ảnh</th>
                                <th>Tên Sản phẩm</th>
                                <th>Giá</th>
                                <th>Tồn kho</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td><img src="https://via.placeholder.com/50" alt="Đồng hồ" class="product-image-small"></td>
                                <td>Carnival 40mm Nam 8807G-VT-D</td>
                                <td>3.801.000 ₫</td>
                                <td>50</td>
                                <td><span class="status active">Hiển thị</span></td>
                                <td>
                                    <button class="btn-icon edit"><i class="fas fa-edit"></i></button>
                                    <button class="btn-icon delete"><i class="fas fa-trash"></i></button>
                                </td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td><img src="https://via.placeholder.com/50" alt="Đồng hồ" class="product-image-small"></td>
                                <td>Casio MTP-1374L-1A</td>
                                <td>1.816.000 ₫</td>
                                <td>120</td>
                                <td><span class="status active">Hiển thị</span></td>
                                <td>
                                    <button class="btn-icon edit"><i class="fas fa-edit"></i></button>
                                    <button class="btn-icon delete"><i class="fas fa-trash"></i></button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div id="page-add-product" class="page-content">
                <div class="page-header">
                    <h1 id="form-title">Thêm sản phẩm mới</h1>
                </div>

                <div class="card">
                    <form id="product-form">
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="product-name">Tên sản phẩm</label>
                                <input type="text" id="product-name" required>
                            </div>
                            <div class="form-group">
                                <label for="product-sku">Mã SKU</label>
                                <input type="text" id="product-sku">
                            </div>
                            <div class="form-group">
                                <label for="product-price">Giá (₫)</label>
                                <input type="number" id="product-price" required>
                            </div>
                            <div class="form-group">
                                <label for="product-stock">Tồn kho</label>
                                <input type="number" id="product-stock" value="0">
                            </div>
                            <div class="form-group full-width">
                                <label for="product-description">Mô tả</label>
                                <textarea id="product-description"></textarea>
                            </div>

                            <div class="form-actions">
                                <button type="button" class="btn btn-secondary" id="btn-cancel-product">Hủy</button>
                                <button type="submit" class="btn btn-primary">Lưu sản phẩm</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <div id="page-orders" class="page-content">
                <div class="page-header">
                    <h1>Quản lý Đơn hàng</h1>
                </div>

                <div class="data-table">
                    <h2>Danh sách tất cả đơn hàng</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Mã Đơn</th>
                                <th>Khách hàng</th>
                                <th>Ngày đặt</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>#1256</td>
                                <td>Nguyễn Văn A</td>
                                <td>11/11/2025</td>
                                <td>12.457.000 ₫</td>
                                <td><span class="status pending">Chờ xử lý</span></td>
                                <td>
                                    <button class="btn-icon view"><i class="fas fa-eye"></i></button>
                                </td>
                            </tr>
                            <tr>
                                <td>#1255</td>
                                <td>Trần Thị B</td>
                                <td>10/11/2025</td>
                                <td>3.801.000 ₫</td>
                                <td><span class="status completed">Đã giao</span></td>
                                <td>
                                    <button class="btn-icon view"><i class="fas fa-eye"></i></button>
                                </td>
                            </tr>
                            <tr>
                                <td>#1254</td>
                                <td>Lê Văn C</td>
                                <td>10/11/2025</td>
                                <td>6.880.000 ₫</td>
                                <td><span class="status cancelled">Đã hủy</span></td>
                                <td>
                                    <button class="btn-icon view"><i class="fas fa-eye"></i></button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div id="page-users" class="page-content">
                <div class="page-header">
                    <h1>Quản lý Khách hàng</h1>
                </div>

                <div class="data-table">
                    <h2>Danh sách khách hàng</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên Khách hàng</th>
                                <th>Email</th>
                                <th>Số điện thoại</th>
                                <th>Ngày đăng ký</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>Nguyễn Văn A</td>
                                <td>nguyenvana@email.com</td>
                                <td>0901234567</td>
                                <td>11/11/2025</td>
                                <td>
                                    <button class="btn-icon edit"><i class="fas fa-edit"></i></button>
                                    <button class="btn-icon delete"><i class="fas fa-trash"></i></button>
                                </td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>Trần Thị B</td>
                                <td>tranthib@email.com</td>
                                <td>0907654321</td>
                                <td>10/11/2025</td>
                                <td>
                                    <button class="btn-icon edit"><i class="fas fa-edit"></i></button>
                                    <button class="btn-icon delete"><i class="fas fa-trash"></i></button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div id="page-settings" class="page-content">
                <div class="page-header">
                    <h1>Cài đặt</h1>
                </div>

                <div class="card" style="margin-bottom: 20px;">
                    <h2>Thông tin Cửa hàng</h2>
                    <form class="form-grid">
                        <div class="form-group full-width">
                            <label for="setting-store-name">Tên cửa hàng</label>
                            <input type="text" id="setting-store-name" value="WatchStore">
                        </div>
                        <div class="form-group">
                            <label for="setting-store-email">Email liên hệ</label>
                            <input type="email" id="setting-store-email" value="contact@watchstore.com">
                        </div>
                        <div class="form-group">
                            <label for="setting-store-phone">Hotline</label>
                            <input type="tel" id="setting-store-phone" value="090 123 4567">
                        </div>
                        <div class="form-group full-width">
                            <label for="setting-store-address">Địa chỉ</label>
                            <input type="text" id="setting-store-address" value="123 Đường ABC, Quận 1, TP. HCM">
                        </div>
                    </form>
                </div>

                <div class="card">
                    <h2>Tài khoản Admin</h2>
                    <form class="form-grid">
                        <div class="form-group">
                            <label for="setting-admin-user">Tên đăng nhập</label>
                            <input type="text" id="setting-admin-user" value="admin" disabled>
                        </div>
                        <div class="form-group">
                            <label for="setting-admin-pass">Mật khẩu mới</label>
                            <input type="password" id="setting-admin-pass" placeholder="Bỏ trống nếu không đổi">
                        </div>
                    </form>
                </div>

                <div style="margin-top: 20px; text-align: right;">
                    <button class="btn btn-primary">
                        <i class="fas fa-save"></i> Lưu thay đổi
                    </button>
                </div>
            </div>


        </main>
    </div>

    <script src="js/admin.js"></script>
</body>
</html>