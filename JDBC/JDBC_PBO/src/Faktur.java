import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Faktur {
    protected String noFaktur;
    protected Akun kasir;
    protected Date tanggalFaktur; // Variabel untuk mencatat tanggal transaksi

    // Daftar transaksi yang terkait dengan faktur ini
    private List<Transaksi> daftarTransaksi = new ArrayList<>();
    
    // Riwayat penjualan disimpan dalam list statis
    private static List<Faktur> riwayat = new ArrayList<>(); 

    // Konstruktor untuk menginisialisasi objek Faktur
    public Faktur(String noFaktur, Akun kasir) {
        this.noFaktur = noFaktur;
        this.kasir = kasir;
        this.tanggalFaktur = new Date(); // Tanggal dan waktu sekarang
        this.daftarTransaksi = new ArrayList<>(); // Inisialisasi daftar transaksi
    }

    // Menambahkan transaksi ke dalam faktur
    public void tambahTransaksi(Transaksi transaksi) {
        this.daftarTransaksi.add(transaksi);
    }

    // Menghitung total transaksi untuk faktur ini
    public int hitungTotal() {
        int total = 0;
        for (Transaksi transaksi : daftarTransaksi) {
            total += transaksi.hitungTotal();  // Menghitung total untuk setiap transaksi
        }
        return total;
    }

    // Menambahkan faktur ke dalam riwayat penjualan dan menyimpannya ke database
    public static void tambahFaktur(Faktur faktur) {
        riwayat.add(faktur);
    
        // Hitung total transaksi dari faktur
        int totalTransaksi = faktur.hitungTotal();
    
        // Simpan faktur ke database, termasuk totaltransaksi
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO riwayat_penjualan (no_faktur, kasir, tanggal, totaltransaksi) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, faktur.getNoFaktur());
            stmt.setString(2, faktur.getKasir().getNamaKasir());
            stmt.setTimestamp(3, new java.sql.Timestamp(faktur.getTanggalFaktur().getTime()));
            stmt.setInt(4, totalTransaksi); // Menyimpan total transaksi
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saat menyimpan faktur: " + e.getMessage());
        }
    }
    
    // Menampilkan riwayat penjualan
    public static void tampilkanRiwayat() {
        System.out.println("\nRiwayat Penjualan:");
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }
        for (Faktur faktur : riwayat) {
            System.out.println("Faktur No: " + faktur.getNoFaktur());
            System.out.println("Tanggal: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(faktur.getTanggalFaktur()));
            System.out.println("Kasir: " + faktur.getKasir().getNamaKasir());
            System.out.println("Total Transaksi: Rp " + faktur.hitungTotal());  // Menampilkan total transaksi
            System.out.println("-----------------------------------");
        }
    }

    // Menampilkan faktur
    public void cetakFaktur() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // Format tanggal yang diinginkan
        System.out.println("[[-------ALPHA TECH's INVOICE------]]");
        System.out.println("Faktur No: " + noFaktur);
        System.out.println("Kasir: " + kasir.getNamaKasir());
        System.out.println("Tanggal Transaksi: " + sdf.format(tanggalFaktur)); // Menampilkan tanggal transaksi dalam format terstruktur
        System.out.println("-----------------------------------");

        // Menampilkan daftar transaksi
        for (Transaksi transaksi : daftarTransaksi) {
            transaksi.displayTransaksi(); // Menampilkan informasi setiap transaksi
            System.out.println("-----------------------------------");
        }

        // Menampilkan total harga transaksi
        int total = daftarTransaksi.stream().mapToInt(Transaksi::hitungTotal).sum();
        System.out.println("Total: Rp " + total);
        System.out.println("[[------------------------------------]]");
    }

    // Getter untuk daftar transaksi
    public List<Transaksi> getDaftarTransaksi() {
        return this.daftarTransaksi; // Mengembalikan daftar transaksi dari objek Faktur
    }

    // Getter untuk tanggal transaksi
    public Date getTanggalFaktur() {
        return tanggalFaktur;
    }

    // Getter untuk noFaktur
    public String getNoFaktur() {
        return noFaktur;
    }
    
    // Getter untuk kasir
    public Akun getKasir() {
        return kasir;
    }
}
