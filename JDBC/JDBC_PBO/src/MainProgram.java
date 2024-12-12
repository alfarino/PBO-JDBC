import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.text.SimpleDateFormat; // Untuk memformat tanggal 

public class MainProgram {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                // Login
                System.out.println("Login");
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                // Captcha
                String captcha = generateCaptcha(6);
                System.out.println("Captcha: " + captcha);
                System.out.print("Masukkan captcha: ");
                String captchaInput = scanner.nextLine();

                if (!captcha.equals(captchaInput)) {
                    System.out.println("Captcha salah. Program ditutup.");
                    return;
                }

                Akun kasir = Akun.autentikasi(username, password);
                if (kasir == null) {
                    System.out.println("Login gagal. Program ditutup.");
                    return;
                }
                System.out.println();
                System.out.println("Login berhasil. Selamat datang, " + kasir.getNamaKasir() + "!");
                
                // Menu pilihan
                String pilihanMenu = "";
                do {
                    System.out.println("\n=== Menu ===");
                    System.out.println("1. Menu Transaksi Pelanggan");
                    System.out.println("2. Menu Tambah Barang");
                    System.out.println("3. Menu Hapus Barang");
                    System.out.println("4. Menu Update Barang");
                    System.out.println("5. Menu Tampilkan Riwayat Transaksi");
                    System.out.println("6. Menu Ganti Kasir");
                    System.out.print("Pilih menu (1-6): ");
                    pilihanMenu = scanner.nextLine().trim();

                    switch (pilihanMenu) {
                        case "1":
                            // Menu Transaksi Pelanggan
                            menuTransaksi(scanner, kasir);
                            break;
                        case "2":
                            // Menu Tambah Barang
                            menuTambahBarang(scanner);
                            break;
                        case "3":
                            // Menu Hapus Barang
                            menuHapusBarang(scanner);
                            break;
                        case "4":
                            // Menu Update Barang
                            menuUpdateBarang(scanner);
                            break;
                        case "5":
                            // Menu Tampilkan Riwayat Transaksi
                            Faktur.tampilkanRiwayat();
                            break;
                        case "6":
                            // Menu Ganti Kasir
                            System.out.println("Ganti Kasir. Program akan keluar.");
                            return;
                        default:
                            System.out.println("Pilihan tidak valid. Silakan pilih antara 1-6.");
                    }

                } while (!pilihanMenu.equals("6"));

            }
        } catch (Exception e) {
            System.out.println("Kesalahan tak terduga: " + e.getMessage());
        }
    }

    // Menu Transaksi Pelanggan
    private static void menuTransaksi(Scanner scanner, Akun kasir) {
        DaftarBarang.tampilkanDaftarBarang();

        List<Transaksi> daftarTransaksi = new ArrayList<>();
        String pilihan = "";

        do {
            String kodeBarang = "";
            while (true) {
                try {
                    System.out.print("\nMasukkan Kode Barang: ");
                    kodeBarang = scanner.nextLine();
                    Barang barangValid = DaftarBarang.validasiBarang(kodeBarang);
                    barangValid.displayInfo();

                    System.out.print("\nJumlah Barang: ");
                    int jumlahBeli = Integer.parseInt(scanner.nextLine());
                    if (jumlahBeli <= 0) {
                        System.out.println("Jumlah beli harus lebih dari 0!");
                        continue;
                    }

                    daftarTransaksi.add(new Transaksi(barangValid.getKodeBarang(), barangValid.getNamaBarang(), barangValid.getHargaBarang(), jumlahBeli));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println("Kesalahan: " + e.getMessage());
                }
            }
            System.out.println();
            System.out.print("Ada pesanan lain? (Y/N): ");
            pilihan = scanner.nextLine().trim().toUpperCase();
            System.out.println();
        } while (pilihan.equals("Y"));

        if (daftarTransaksi.isEmpty()) {
            System.out.println("Tidak ada transaksi yang diproses.");
            return;
        }

        String noFaktur = generateNoFaktur();
        Faktur faktur = new Faktur(noFaktur, kasir);
        for (Transaksi transaksi : daftarTransaksi) {
            faktur.tambahTransaksi(transaksi);
        }

        faktur.cetakFaktur();
        Faktur.tambahFaktur(faktur);
    }

    // Menu Tambah Barang
    private static void menuTambahBarang(Scanner scanner) {
        System.out.print("\nMasukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine();
        System.out.print("Masukkan Nama Barang: ");
        String namaBarang = scanner.nextLine();
        System.out.print("Masukkan Harga Barang: ");
        int hargaBarang = Integer.parseInt(scanner.nextLine());

        Barang barang = new Barang(kodeBarang, namaBarang, hargaBarang);
        Barang.tambahBarang(barang);
    }

    // Menu Hapus Barang
    private static void menuHapusBarang(Scanner scanner) {
        System.out.print("\nMasukkan Kode Barang yang ingin dihapus: ");
        String kodeBarang = scanner.nextLine();
        Barang.deleteBarang(kodeBarang);
    }

    // Menu Update Barang
    private static void menuUpdateBarang(Scanner scanner) {
        System.out.print("\nMasukkan Kode Barang yang ingin diubah: ");
        String kodeBarang = scanner.nextLine();

        try {
            Barang barang = DaftarBarang.validasiBarang(kodeBarang);
            System.out.println("Barang ditemukan: ");
            barang.displayInfo();

            System.out.print("\nMasukkan Nama Baru (kosongkan jika tidak ingin mengubah): ");
            String namaBaru = scanner.nextLine();
            if (!namaBaru.isEmpty()) {
                barang.setNamaBarang(namaBaru);
            }

            System.out.print("Masukkan Harga Baru (kosongkan jika tidak ingin mengubah): ");
            String hargaBaru = scanner.nextLine();
            if (!hargaBaru.isEmpty()) {
                barang.setHargaBarang(Integer.parseInt(hargaBaru));
            }

            Barang.updateBarang(barang);
        } catch (IllegalArgumentException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }
    }

    // Generate Captcha
    private static String generateCaptcha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }

    // Generate No Faktur
    private static String generateNoFaktur() {
        return "F" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + String.format("%04d", new Random().nextInt(10000));
    }
}
