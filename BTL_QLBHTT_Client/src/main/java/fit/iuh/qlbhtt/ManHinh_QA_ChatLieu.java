/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package fit.iuh.qlbhtt;

import fit.iuh.dao.IChatLieuDao;
import fit.iuh.entity.*;
import fit.iuh.util.RMIClientUtil;

import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author DMX
 */
public   class ManHinh_QA_ChatLieu extends javax.swing.JPanel {

    private IChatLieuDao dao_ChatLieu;
    private DefaultTableModel modelChatLieu;
    private boolean kiemTraThem = false;
    private boolean kiemTraCapNhat = false;

    /**
     * Creates new form quanly
     */
    public   ManHinh_QA_ChatLieu() throws SQLException, RemoteException {
        dao_ChatLieu = RMIClientUtil.getChatLieuDao();
        initComponents();
        
        tbl_ChatLieu.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_ChatLieu.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột
        
        docDuLieuLenBang();
    }

    /**
     * Huy thao tac hoat dong cua componet
     */
    private synchronized  void huyThaoTacNhap() {
        kiemTraCapNhat = false;
        kiemTraThem = false;
        btn_CapNhat.setText("Cập nhật");
        btn_Them.setText("Thêm");
        btn_Luu.setEnabled(false);
        btn_CapNhat.setEnabled(true);
        btn_Them.setEnabled(true);
        btn_TimKiem.setEnabled(true);
        kiemTraTextNhap(true);
        xoaTrang();
    }

    /**
     * Kiem tra hoat dong cua cac JtextField
     */
    public synchronized  void kiemTraTextNhap(boolean kiemTra) {
        if (kiemTraCapNhat || kiemTraThem) {
            txt_MaChatLieu.setEditable(!kiemTra);
        } else {
            txt_MaChatLieu.setEditable(kiemTra);
        }

    }

    /**
     * Xóa trắng text field
     */
    public synchronized  void xoaTrang() {
        txt_MaChatLieu.setText("");
        txt_TenChatLieu.setText("");
    }

    /**
     * Đọc dữ liệu và load dữ liệu lên table
     */
    public synchronized  void docDuLieuLenBang() throws RemoteException {
        modelChatLieu = (DefaultTableModel) tbl_ChatLieu.getModel();
        modelChatLieu.setRowCount(0);
        for (ChatLieu chatLieu : dao_ChatLieu.getAllChatLieu()) {
            Object[] o = new Object[2];
            o[0] = chatLieu.getMaChatLieu();
            o[1] = chatLieu.getChatLieu();
            modelChatLieu.addRow(o);
        }
    }

    /**
     * Xử lý thêm Chất Liệu
     */
    public synchronized  void xuLyThemChatLieu() throws RemoteException {
        String tenChatLieu = txt_TenChatLieu.getText();

        ChatLieu chatLieu = new ChatLieu(tenChatLieu);
        dao_ChatLieu.themDLChatLieu(chatLieu);

        modelChatLieu = (DefaultTableModel) tbl_ChatLieu.getModel();
        Object[] object = new Object[2];
        object[0] = chatLieu.getMaChatLieu();
        object[1] = chatLieu.getChatLieu();
        modelChatLieu.addRow(object);
        docDuLieuLenBang();
        xoaTrang();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
    }

    /**
     * Xử lý xóa Chất Liệu
     */
    public synchronized  void xuLyXoaChatLieu() throws RemoteException {
        int row = tbl_ChatLieu.getSelectedRow();
        if (row != -1) {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc là xóa dòng này không?", "Cảnh Báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                long maChatLieu = Long.parseLong(txt_MaChatLieu.getText());
                dao_ChatLieu.xoaDLChatLieu(maChatLieu);
                modelChatLieu.removeRow(row);
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                xoaTrang();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
        }
    }

    /**
     * Xử lý cập nhật Chất Liệu
     */
    public synchronized  void xuLyCapNhatChatLieu() throws RemoteException {
        long maChatLieu = Long.parseLong(txt_MaChatLieu.getText());
        String tenChatLieu = txt_TenChatLieu.getText();

        ChatLieu chatLieu = new ChatLieu(maChatLieu,tenChatLieu);
        int row = tbl_ChatLieu.getSelectedRow();
        if (row != -1) {
            dao_ChatLieu.catNhatDLChatLieu(chatLieu);
            for (int i = 0; i < tbl_ChatLieu.getRowCount(); i++) {
                long maChatLieuTable = Long.parseLong(tbl_ChatLieu.getValueAt(row, 0).toString());
                if (maChatLieuTable==maChatLieu) {
                    tbl_ChatLieu.setValueAt(tenChatLieu, row, 1);
                }

            }
            xoaTrang();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật!");
        }

    }

    /**
     * Tìm kiếm chất liệu
     */
    public synchronized  void xuLyTimKiemChatLieu() throws RemoteException {
        String msConvert = null;
        if(txt_MaChatLieu.getText().equals("")) {
            msConvert = "0";
        } else {
            msConvert = txt_MaChatLieu.getText();
        }
        long tuKhoaMaChatLieu = Long.parseLong(msConvert);
        String tuKhoaTenChatLieu = txt_TenChatLieu.getText();

        if(tuKhoaMaChatLieu == 0 && tuKhoaTenChatLieu.equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        String maCLString = null;
        if(tuKhoaMaChatLieu == 0 ){
            maCLString = "";
        }

        modelChatLieu = (DefaultTableModel) tbl_ChatLieu.getModel();
        modelChatLieu.setRowCount(0);
        if(maCLString != null) {
            if (tuKhoaTenChatLieu.equals("")) {
                ChatLieu chatLieu = dao_ChatLieu.getDLChatLieuTheoMa(tuKhoaMaChatLieu);
                if (chatLieu != null) {
                    modelChatLieu.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = chatLieu.getMaChatLieu();
                    object[1] = chatLieu.getChatLieu();
                    modelChatLieu.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu");
                }
            } else if (maCLString.equals("")) {
                ChatLieu chatLieu = dao_ChatLieu.getChatLieuTheoTen(tuKhoaTenChatLieu);
                if (chatLieu != null) {
                    modelChatLieu.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = chatLieu.getMaChatLieu();
                    object[1] = chatLieu.getChatLieu();
                    modelChatLieu.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu");
                }
            }
        } else {
            ChatLieu chatLieu = dao_ChatLieu.getDLChatLieuTheoMa(tuKhoaMaChatLieu);
            if (chatLieu != null) {
                modelChatLieu.setRowCount(0);
                Object[] object = new Object[2];
                object[0] = chatLieu.getMaChatLieu();
                object[1] = chatLieu.getChatLieu();
                modelChatLieu.addRow(object);
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private synchronized  void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        pnl_DanhSachChatLieu = new javax.swing.JPanel();
        scr_DanhSachChatLieu = new javax.swing.JScrollPane();
        tbl_ChatLieu = new javax.swing.JTable();
        pnl_CL_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        txt_MaChatLieu = new javax.swing.JTextField();
        lbl_MaChatLieu = new javax.swing.JLabel();
        lbl_TenChatLieu = new javax.swing.JLabel();
        txt_TenChatLieu = new javax.swing.JTextField();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_Them = new javax.swing.JButton();
        btn_CapNhat = new javax.swing.JButton();
        btn_Luu = new javax.swing.JButton();
        btn_XoaTrang = new javax.swing.JButton();
        btn_TimKiem = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        pnl_DanhSachChatLieu.setPreferredSize(new java.awt.Dimension(2000, 324));

        scr_DanhSachChatLieu.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách chất liệu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_ChatLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_ChatLieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã chất liệu", "Tên chất liệu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public synchronized  Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_ChatLieu.setMaximumSize(new java.awt.Dimension(2147483647, 196));
        tbl_ChatLieu.setPreferredSize(new java.awt.Dimension(750, 600));
        tbl_ChatLieu.setRowHeight(35);
        tbl_ChatLieu.setShowGrid(true);
        tbl_ChatLieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ChatLieuMouseClicked(evt);
            }
        });
        scr_DanhSachChatLieu.setViewportView(tbl_ChatLieu);

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("QUẢN LÝ CHẤT LIỆU");
        lbl_TieuDe.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lbl_TieuDe.setPreferredSize(new java.awt.Dimension(181, 40));

        javax.swing.GroupLayout pnl_CL_TieuDeLayout = new javax.swing.GroupLayout(pnl_CL_TieuDe);
        pnl_CL_TieuDe.setLayout(pnl_CL_TieuDeLayout);
        pnl_CL_TieuDeLayout.setHorizontalGroup(
            pnl_CL_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_CL_TieuDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnl_CL_TieuDeLayout.setVerticalGroup(
            pnl_CL_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_DanhSachChatLieuLayout = new javax.swing.GroupLayout(pnl_DanhSachChatLieu);
        pnl_DanhSachChatLieu.setLayout(pnl_DanhSachChatLieuLayout);
        pnl_DanhSachChatLieuLayout.setHorizontalGroup(
            pnl_DanhSachChatLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_CL_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scr_DanhSachChatLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        pnl_DanhSachChatLieuLayout.setVerticalGroup(
            pnl_DanhSachChatLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_DanhSachChatLieuLayout.createSequentialGroup()
                .addComponent(pnl_CL_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scr_DanhSachChatLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chất liệu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        pnl_ThongTin.setPreferredSize(new java.awt.Dimension(945, 285));

        txt_MaChatLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_MaChatLieu.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_MaChatLieu.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_MaChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MaChatLieuActionPerformed(evt);
            }
        });

        lbl_MaChatLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MaChatLieu.setText("Mã chất liệu");

        lbl_TenChatLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenChatLieu.setText("Tên chất liệu");

        txt_TenChatLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_TenChatLieu.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_TenChatLieu.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_TenChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenChatLieuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_MaChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_MaChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(106, 106, 106)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_TenChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TenChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_ThongTinLayout.setVerticalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                        .addComponent(lbl_TenChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_TenChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                        .addComponent(lbl_MaChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_MaChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(184, Short.MAX_VALUE))
        );

        pnl_NutChucNang.setBackground(new java.awt.Color(199, 210, 213));
        pnl_NutChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        btn_Them.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Them.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-add-30.png")); // NOI18N
        btn_Them.setText("Thêm");
        btn_Them.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Them.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ThemMouseEntered(evt);
            }
            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ThemMouseExited(evt);
            }
        });
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_CapNhat.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_CapNhat.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-update-30.png")); // NOI18N
        btn_CapNhat.setText("Cập nhật");
        btn_CapNhat.setBorder(null);
        btn_CapNhat.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_CapNhatMouseEntered(evt);
            }
            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_CapNhatMouseExited(evt);
            }
        });
        btn_CapNhat.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhatActionPerformed(evt);
            }
        });

        btn_Luu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Luu.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-save-30.png")); // NOI18N
        btn_Luu.setText("Lưu");
        btn_Luu.setBorder(null);
        btn_Luu.setEnabled(false);
        btn_Luu.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_LuuMouseEntered(evt);
            }
            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_LuuMouseExited(evt);
            }
        });
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LuuActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_XoaTrang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_XoaTrang.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-delete-30.png")); // NOI18N
        btn_XoaTrang.setText("Xóa trắng");
        btn_XoaTrang.setBorder(null);
        btn_XoaTrang.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_XoaTrangMouseEntered(evt);
            }
            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_XoaTrangMouseExited(evt);
            }
        });
        btn_XoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_XoaTrangActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_TimKiem.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-search-30.png")); // NOI18N
        btn_TimKiem.setText("Tìm kiếm");
        btn_TimKiem.setBorder(null);
        btn_TimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_TimKiemMouseEntered(evt);
            }
            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_TimKiemMouseExited(evt);
            }
        });
        btn_TimKiem.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_TimKiemActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout pnl_NutChucNangLayout = new javax.swing.GroupLayout(pnl_NutChucNang);
        pnl_NutChucNang.setLayout(pnl_NutChucNangLayout);
        pnl_NutChucNangLayout.setHorizontalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_TimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_CapNhat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Them, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Luu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_XoaTrang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        pnl_NutChucNangLayout.setVerticalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_CapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_XoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pnl_DanhSachChatLieu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_DanhSachChatLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized  void txt_MaChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MaChatLieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaChatLieuActionPerformed

    private synchronized  void btn_CapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhatActionPerformed
        if (btn_CapNhat.getText().equalsIgnoreCase("Cập nhật")) {
            btn_CapNhat.setText("Hủy");
            btn_Them.setEnabled(false);
            btn_Luu.setEnabled(true);
            btn_TimKiem.setEnabled(false);
            kiemTraCapNhat = true;
            kiemTraTextNhap(true);
            xoaTrang();
        } else if (btn_CapNhat.getText().equalsIgnoreCase("Hủy")) {
            btn_CapNhat.setText("Cập nhật");
            huyThaoTacNhap();
        }
    }//GEN-LAST:event_btn_CapNhatActionPerformed

    private synchronized  void btn_LuuActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_LuuActionPerformed
        if (kiemTraThem) {
            xuLyThemChatLieu();
        } else if (kiemTraCapNhat) {
            xuLyCapNhatChatLieu();
        }
    }//GEN-LAST:event_btn_LuuActionPerformed

    private synchronized  void txt_TenChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenChatLieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenChatLieuActionPerformed

    private synchronized  void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        if (btn_Them.getText().equalsIgnoreCase("Thêm")) {
            btn_Them.setText("Hủy");
            btn_CapNhat.setEnabled(false);
            btn_Luu.setEnabled(true);
            btn_TimKiem.setEnabled(false);
            kiemTraThem = true;
            kiemTraTextNhap(true);
            xoaTrang();
        } else if (btn_Them.getText().equalsIgnoreCase("Hủy")) {
            btn_Them.setText("Thêm");
            huyThaoTacNhap();
        }
    }//GEN-LAST:event_btn_ThemActionPerformed

    private synchronized  void tbl_ChatLieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ChatLieuMouseClicked
        int row = tbl_ChatLieu.getSelectedRow();
        if (row != -1) {
            txt_MaChatLieu.setText(tbl_ChatLieu.getValueAt(row, 0).toString());
            txt_TenChatLieu.setText(tbl_ChatLieu.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_tbl_ChatLieuMouseClicked

    private synchronized  void btn_XoaTrangActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_XoaTrangActionPerformed
        xoaTrang();
        docDuLieuLenBang();
    }//GEN-LAST:event_btn_XoaTrangActionPerformed

    private synchronized  void btn_ThemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemMouseEntered
        if (btn_Them.isEnabled()) {
            btn_Them.setBackground(new Color(0x9EDDFF));
            btn_Them.setForeground(new Color(0x141E46));
        }
    }//GEN-LAST:event_btn_ThemMouseEntered

    private synchronized  void btn_ThemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemMouseExited
        if (btn_Them.isEnabled()) {
            btn_Them.setBackground(UIManager.getColor("Menu.background"));
            btn_Them.setForeground(UIManager.getColor("Menu.foreground"));
        }
    }//GEN-LAST:event_btn_ThemMouseExited

    private synchronized  void btn_CapNhatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CapNhatMouseEntered
        if (btn_CapNhat.isEnabled()) {
            btn_CapNhat.setBackground(new Color(0x9EDDFF));
            btn_CapNhat.setForeground(new Color(0x141E46));
        }

    }//GEN-LAST:event_btn_CapNhatMouseEntered

    private synchronized  void btn_CapNhatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CapNhatMouseExited
        if (btn_CapNhat.isEnabled()) {
            btn_CapNhat.setBackground(UIManager.getColor("Menu.background"));
            btn_CapNhat.setForeground(UIManager.getColor("Menu.foreground"));
        }

    }//GEN-LAST:event_btn_CapNhatMouseExited

    private synchronized  void btn_LuuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LuuMouseEntered
        if (btn_Luu.isEnabled()) {
            btn_Luu.setBackground(new Color(0x9EDDFF));
            btn_Luu.setForeground(new Color(0x141E46));
        }

    }//GEN-LAST:event_btn_LuuMouseEntered

    private synchronized  void btn_LuuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LuuMouseExited
        if (btn_Luu.isEnabled()) {
            btn_Luu.setBackground(UIManager.getColor("Menu.background"));
            btn_Luu.setForeground(UIManager.getColor("Menu.foreground"));
        }

    }//GEN-LAST:event_btn_LuuMouseExited

    private synchronized  void btn_XoaTrangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XoaTrangMouseEntered
        btn_XoaTrang.setBackground(new Color(0x9EDDFF));
        btn_XoaTrang.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_XoaTrangMouseEntered

    private synchronized  void btn_XoaTrangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XoaTrangMouseExited
        btn_XoaTrang.setBackground(UIManager.getColor("Menu.background"));
        btn_XoaTrang.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_XoaTrangMouseExited

    private synchronized  void btn_TimKiemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_TimKiemMouseEntered
        if (btn_TimKiem.isEnabled()) {
            btn_TimKiem.setBackground(new Color(0x9EDDFF));
            btn_TimKiem.setForeground(new Color(0x141E46));
        }
    }//GEN-LAST:event_btn_TimKiemMouseEntered

    private synchronized  void btn_TimKiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_TimKiemMouseExited
        if (btn_TimKiem.isEnabled()) {
            btn_TimKiem.setBackground(UIManager.getColor("Menu.background"));
            btn_TimKiem.setForeground(UIManager.getColor("Menu.foreground"));
        }
    }//GEN-LAST:event_btn_TimKiemMouseExited

    private synchronized  void btn_TimKiemActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_TimKiemActionPerformed
        xuLyTimKiemChatLieu();
    }//GEN-LAST:event_btn_TimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CapNhat;
    private javax.swing.JButton btn_Luu;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_XoaTrang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lbl_MaChatLieu;
    private javax.swing.JLabel lbl_TenChatLieu;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JPanel pnl_CL_TieuDe;
    private javax.swing.JPanel pnl_DanhSachChatLieu;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JScrollPane scr_DanhSachChatLieu;
    private javax.swing.JTable tbl_ChatLieu;
    private javax.swing.JTextField txt_MaChatLieu;
    private javax.swing.JTextField txt_TenChatLieu;
    // End of variables declaration//GEN-END:variables

}
