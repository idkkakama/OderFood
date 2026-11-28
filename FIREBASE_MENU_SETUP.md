# Thiết lập Firebase cho chức năng thực đơn và gọi món

1. Mở Firebase Console > Firestore Database và tạo database nếu dự án chưa có.
2. Mở tab Rules, sao chép nội dung file `firestore.rules` ở thư mục gốc dự án rồi Publish.
3. Đăng nhập ứng dụng bằng tài khoản đã đăng ký với vai trò `owner` để vào Quản lý > Thêm món ăn.
4. Tài khoản nhân viên (`staff`) mở tab Thực đơn, chọn số lượng bằng `+ Thêm` / `−`, sau đó nhấn `Xác nhận gọi món`.
5. Firestore sử dụng hai collection: `foods` và `orders`. Trường `image` của món ăn là tên drawable hiện có, ví dụ `food_beef`, `food_chicken`, `food_crab_soup`, `food_catfish`, `food_spring_rolls`, `food_pandan_tea`.

## Cấu trúc foods
- `name`: string
- `price`: number (VNĐ)
- `category`: string
- `image`: string (tên drawable, không có phần mở rộng)
- `description`: string
- `available`: boolean
- `updatedAt`: timestamp

## Cấu trúc orders
- `tableId`: string
- `items`: array các object `{ foodId, name, price, quantity, note }`
- `total`: number
- `status`: `pending` khi mới gửi
- `createdAt`: timestamp
- `createdBy`: UID nhân viên tạo đơn

Lưu ý: hiện tại màn hình gọi món lấy mã bàn từ Intent `tableId`; nếu mở trực tiếp từ tab Thực đơn thì mặc định là `Bàn 04`. Khi tích hợp với màn hình bàn, truyền đúng mã bàn thực tế vào Intent.
