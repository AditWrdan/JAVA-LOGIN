import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrm extends JDialog {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel LoginPanel;
    private JPanel loginPanel;

    public LoginFrm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                getAunthentacatedUser(email, password);

                user = getAunthentacatedUser(email, password);

                if (user != null){
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginFrm.this,
                            "Email or Password Invalid",
                            "try again",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
    public USER user;
    private USER getAunthentacatedUser(String email, String password){
        USER user = null;

        final String DB_URL ="jdbc:mysql://localhost/projet1?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connected ke databes sukses

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatment = conn.prepareStatement(sql);
            preparedStatment.setString(1, email);
            preparedStatment.setString(2, password);

            ResultSet resultSet = preparedStatment.executeQuery();

            if (resultSet.next()){
                user = new USER();
                user.nama =resultSet.getString("nama");
                user.email =resultSet.getString("email");
                user.phone =resultSet.getString("phone");
                user.alamat =resultSet.getString("alamat");
                user.password =resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }
    public static void main(String[] args) {
        LoginFrm loginFrm = new LoginFrm(null);
        USER user =loginFrm.user;
        if (user != null){
            System.out.println("Successful Authentication of : "+user.nama);
            System.out.println("            Email: " +user.email);
            System.out.println("            Phone: " +user.phone);
            System.out.println("            Alamat:" +user.alamat);
        }
        else{
            System.out.println("Authentication Cancelen");
        }

    }
}
