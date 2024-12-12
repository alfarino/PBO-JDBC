import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Akun {
    private String namaKasir;

    // Konstruktor untuk menginisialisasi objek Akun
    public Akun(String username, String password, String namaKasir) {
        this.namaKasir = namaKasir;
    }

    // Getter untuk nama kasir
    public String getNamaKasir() {
        return namaKasir;
    }

    // Metode untuk autentikasi, mengambil data dari database
    public static Akun autentikasi(String username, String password) {
        String query = "SELECT * FROM akun WHERE username = ? AND password = ?";
        Akun akun = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Menyiapkan parameter untuk query
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Eksekusi query dan ambil hasilnya
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Jika data ditemukan, buat objek Akun
                String namaKasir = rs.getString("nama_kasir");
                akun = new Akun(username, password, namaKasir);
            }

        } catch (SQLException e) {
            System.out.println("Error saat autentikasi: " + e.getMessage());
        }

        return akun; // Mengembalikan akun jika autentikasi berhasil, null jika gagal
    }
}
