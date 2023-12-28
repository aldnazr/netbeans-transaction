package TokoHp.Views;

import TokoHp.Objects.Variable;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class RiwayatTransaksi extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement preparedStatement;
    String sql;
    ResultSet resultSet;
    int executeResult;
    CallableStatement callableStatement;
    DefaultTableModel tableModel;

    public RiwayatTransaksi() {
        initComponents();
        init();
    }

    private void init() {
        connection = Variable.koneksi();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setSearchFieldIcon(tfPencarian);
        setTableData();
        getTransaksi();
        getTerjual();
        card1.setCorner(26);
        card3.setCorner(26);
        jLabel2.setIcon(new FlatSVGIcon("TokoHp/Icons/riwayat_transaksi/shopping_bag.svg", 1.4f));
        jLabel3.setIcon(new FlatSVGIcon("TokoHp/Icons/riwayat_transaksi/box.svg", 1.4f));
        iconDate.setIcon(new FlatSVGIcon("TokoHp/Icons/riwayat_transaksi/calendar_month.svg", 0.9f));

        if (Variable.isDarkTheme()) {
            Color darkColor = Color.decode("#363638");
            card1.setForeground(darkColor);
            card2.setBackground(darkColor);
            card3.setForeground(darkColor);
            card4.setBackground(darkColor);
            iconDate.setIcon(new FlatSVGIcon("TokoHp/Icons/riwayat_transaksi/calendar_month_dark.svg", 0.9f));
        }
    }

    private void getTransaksi() {
        try {
            sql = "{? = call transaksiHariIni}";
            callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            executeResult = callableStatement.getInt(1);

            labelTransaksi.setText(String.valueOf(executeResult));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getTerjual() {
        try {
            sql = "{? = call terjualHariIni}";
            callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            executeResult = callableStatement.getInt(1);

            labelProdukTerjual.setText(String.valueOf(executeResult));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void setTableData() {
        tableModel = (DefaultTableModel) tableRiwayat.getModel();
        try {
            sql = switch (cbFilterDate.getSelectedIndex()) {
                case 1 ->
                    Variable.riwayatTransaksi("(TRUNC(T.TANGGAL) >= SYSDATE - 7) AND");
                case 2 ->
                    Variable.riwayatTransaksi("(TRUNC(T.TANGGAL) >= SYSDATE - 14) AND");
                case 3 ->
                    Variable.riwayatTransaksi("(TRUNC(T.TANGGAL) >= SYSDATE - 30) AND");
                default ->
                    Variable.riwayatTransaksi("");
            };
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + tfPencarian.getText() + "%");
            preparedStatement.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            preparedStatement.setString(3, "%" + tfPencarian.getText().toLowerCase() + "%");
            preparedStatement.setString(4, "%" + tfPencarian.getText() + "%");

            resultSet = preparedStatement.executeQuery();
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] data = {
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getTimestamp(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    Variable.stringToNumber(String.valueOf(resultSet.getInt(7))),
                    resultSet.getInt(8),
                    Variable.stringToNumber(String.valueOf(resultSet.getInt(9))),
                    Variable.stringToNumber(String.valueOf(resultSet.getInt(10)))
                };
                tableModel.addRow(data);
            }
            tableRiwayat.setModel(tableModel);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableRiwayat = new javax.swing.JTable();
        tfPencarian = new javax.swing.JTextField();
        card1 = new TokoHp.Component.Card();
        card2 = new TokoHp.Component.Card();
        jLabel2 = new javax.swing.JLabel();
        labelTransaksi = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbFilterDate = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        iconDate = new javax.swing.JLabel();
        card3 = new TokoHp.Component.Card();
        card4 = new TokoHp.Component.Card();
        jLabel3 = new javax.swing.JLabel();
        labelProdukTerjual = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        tableRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Transaksi", "Kasir", "Nama Pelanggan", "Waktu", "Brand", "Model", "Harga Per-Item", "Jumlah Pemelian", "Subtotal", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableRiwayat);
        if (tableRiwayat.getColumnModel().getColumnCount() > 0) {
            tableRiwayat.getColumnModel().getColumn(0).setPreferredWidth(80);
            tableRiwayat.getColumnModel().getColumn(0).setMaxWidth(80);
            tableRiwayat.getColumnModel().getColumn(1).setPreferredWidth(120);
            tableRiwayat.getColumnModel().getColumn(1).setMaxWidth(120);
            tableRiwayat.getColumnModel().getColumn(7).setPreferredWidth(110);
            tableRiwayat.getColumnModel().getColumn(7).setMaxWidth(110);
        }

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        card1.setForeground(new java.awt.Color(255, 255, 255));
        card1.setPreferredSize(new java.awt.Dimension(170, 82));

        card2.setBackground(new java.awt.Color(255, 255, 255));
        card2.setForeground(new java.awt.Color(208, 232, 249));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        labelTransaksi.setFont(new java.awt.Font("Poppins", 1, 22)); // NOI18N
        labelTransaksi.setText("10");

        jLabel8.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        jLabel8.setText("Transaksi");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTransaksi)
                    .addComponent(jLabel8))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                        .addComponent(labelTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        cbFilterDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tanggal", "7 Hari", "14 Hari", "30 Hari" }));
        cbFilterDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFilterDateActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Poppins SemiBold", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Hari ini:");

        iconDate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        iconDate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        card3.setForeground(new java.awt.Color(255, 255, 255));
        card3.setPreferredSize(new java.awt.Dimension(170, 82));

        card4.setBackground(new java.awt.Color(255, 255, 255));
        card4.setForeground(new java.awt.Color(254, 242, 195));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        labelProdukTerjual.setFont(new java.awt.Font("Poppins", 1, 22)); // NOI18N
        labelProdukTerjual.setText("10");

        jLabel7.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        jLabel7.setText("Barang Terjual");

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelProdukTerjual)
                    .addComponent(jLabel7))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card3Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card3Layout.createSequentialGroup()
                        .addComponent(labelProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1159, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(iconDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbFilterDate, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))
                        .addContainerGap(665, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFilterDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        setTableData();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void cbFilterDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterDateActionPerformed
        setTableData();
    }//GEN-LAST:event_cbFilterDateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private TokoHp.Component.Card card1;
    private TokoHp.Component.Card card2;
    private TokoHp.Component.Card card3;
    private TokoHp.Component.Card card4;
    private javax.swing.JComboBox<String> cbFilterDate;
    private javax.swing.JLabel iconDate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelProdukTerjual;
    private javax.swing.JLabel labelTransaksi;
    private javax.swing.JTable tableRiwayat;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
