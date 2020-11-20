import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BtnPanel extends JPanel { // 입출력 UI
    final String BTNPANEL_BG_COLOR = "#9999FF";

    JButton btnInsert, btnDelete;
    JTextField number;

    BtnPanel() {
        number = new JTextField(5); // 숫자 입력 칸
        btnInsert = new JButton("삽입"); // 삽입 버튼
        btnDelete = new JButton("삭제"); // 삭제 버튼

        btnInsert.setSize(60, 40);
        btnDelete.setSize(60, 40); // 버튼 크기 조절

        setOpaque(true);
        setBackground(Color.decode(BTNPANEL_BG_COLOR));

        add(number);
        add(btnInsert);
        add(btnDelete); // 패널에 요소 삽입
    }
}

class TreePanel extends JPanel { // 트리가 그려지는 패널
    final String TREEPANEL_BG_COLOR = "#FFFFFF";
    final String NODE_COLOR = "#FFCC66";
    final String COLOR_BLACK = "#000000";

    TreePanel() {
        setOpaque(true);
        setBackground(Color.decode(TREEPANEL_BG_COLOR));
    }

    public void drawTree(Node n, Node root) { // 트리 그리는 함수
        Graphics dTree = getGraphics();

        if(n == root) {
            super.paintComponent(dTree);
        }

        if(n != null) {
            drawTree(n.getLeft(), n);
            if (n.getLeft() != null && n.getRight() != null) {
                drawLine(n, n.getLeft());
                drawLine(n, n.getRight());
            } else if (n.getLeft() == null && n.getRight() != null) {
                drawLine(n, n.getRight());
            } else if (n.getRight() == null && n.getLeft() != null) {
                drawLine(n, n.getLeft());
            }
            drawNode(n);
            drawTree(n.getRight(), n);
        }
    }

    public void drawNode(Node n) { // 노드 그리는 함수
        Graphics dNode = getGraphics();

        dNode.setColor(Color.decode(NODE_COLOR));
        dNode.fillOval(n.getX_coor(), n.getY_coor(), 40, 40);
        dNode.setColor(Color.decode(COLOR_BLACK));
        dNode.drawString(Integer.toString(n.getValue()), n.getX_coor() + 13, n.getY_coor() + 23);
    }

    public void drawLine(Node p, Node n) { // 간선 그리는 함수
        Graphics line = getGraphics();
        line.setColor(Color.decode(COLOR_BLACK));
        line.drawLine(p.getX_coor() + 20, p.getY_coor() + 40, n.getX_coor() + 20, n.getY_coor());
    }
}

class BST_frame extends JFrame {
    final int MAX_NODE_COUNT = 20; // 최대 노드 갯수 상수
    final int MAX_DEPTH = 4; // 최대 깊이 상수

    BtnPanel btnPanel = new BtnPanel();
    TreePanel treePanel = new TreePanel();

    Node root;
    int nodecnt = 0;

    BST_frame() {
        this.root = null;

        btnPanel.setBounds(0, 840, 1200, 100); // 버튼 패널 크기, 위치 조절
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 버튼 패널 중앙 정렬

        treePanel.setSize(1200, 840);
        treePanel.setBounds(0, 0, 1200, 840);

        add(btnPanel);
        add(treePanel);

        setLayout(null);
        setFocusable(true);

        btnPanel.btnInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tempValue = Integer.parseInt(btnPanel.number.getText());
                if(checkOverlap(tempValue)) { // 중복 값 처리
                    JOptionPane.showMessageDialog(null, "중복된 키를 가진 노드가 존재합니다.", "중복 값 존재", JOptionPane.WARNING_MESSAGE);
                    btnPanel.number.setText(null);
                }
                else if(isFull()) { // 오버플로우 처리
                    JOptionPane.showMessageDialog(null, "노드의 개수는 20을 넘을 수 없습니다.\n노드 삭제 후 다시 시도하세요.", "OVERFLOW", JOptionPane.WARNING_MESSAGE);
                    btnPanel.number.setText(null);
                }
                else { // 삽입
                    insert(tempValue);
                    btnPanel.number.setText(null);
                    nodecnt++;
                }
            }
        });

        btnPanel.btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(btnPanel.number.getText() == null) {
                    JOptionPane.showMessageDialog(null, "삭제할 값을 입력하세요.", "삭제값 없음", JOptionPane.WARNING_MESSAGE);
                } else {
                    int tempValue = Integer.parseInt(btnPanel.number.getText());
                    btnPanel.number.setText("");
                    if(!delete(tempValue)) {
                        JOptionPane.showMessageDialog(null, "삭제할 값은 가진 노드가 존재하지 않습니다.", "노드가 존재하지 않음", JOptionPane.WARNING_MESSAGE);
                    } else {
                        nodecnt--;
                    }
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new BST_frame(); //전체 윈도우 프레임 객체 생성

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(1200, 940);
        frame.setTitle("Binary Search Tree - 2016010887 이수호");
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public boolean isFull() {
        if(nodecnt == MAX_NODE_COUNT) return true;
        else return false;
    }

    public boolean checkOverlap(int value) {
        Node searchNode = root;
        while(searchNode != null) {
            if(value == searchNode.getValue()) return true;
            else if(value < searchNode.getValue()) searchNode = searchNode.getLeft();
            else searchNode = searchNode.getRight();
        }
        return false;
    }

    public boolean isFullDepth(Node n) {
        if(n.getDepth() == MAX_DEPTH) return true;
        else return false;
    }

    public void insert(int value) {
        Node newNode = new Node(value);
        Node searchNode;
        Node parentNode;

        if(root == null) { // 빈 트리 삽입
            root = newNode;
            root.setX_coor(580);
            root.setY_coor(40);
            root.setDepth(0);

            /*JLabel nodeLabel = new JLabel(Integer.toString(value)); // 노드 레이블
            treePanel.add(nodeLabel);
            nodeLabel.setOpaque(true);
            nodeLabel.setBackground(Color.ORANGE);
            nodeLabel.setForeground(Color.BLACK);
            nodeLabel.setHorizontalAlignment(JLabel.CENTER);
            nodeLabel.setVerticalAlignment(JLabel.CENTER);
            nodeLabel.setBounds(root.getX_coor(), root.getY_coor(), 40, 40);*/

            treePanel.drawTree(root, root);

            return;
        }
        searchNode = root;
        while(true) {
            parentNode = searchNode;
            if(value < searchNode.getValue()) { // 왼쪽 자식으로 삽입
                searchNode = searchNode.getLeft();
                if(searchNode == null) {
                    if(isFullDepth(parentNode)) {
                        JOptionPane.showMessageDialog(null, "트리의 깊이는 5를 넘을 수 없습니다.", "최대 깊이 초과", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else {
                        parentNode.setLeft(newNode);
                        newNode.setDepth(parentNode.getDepth() + 1);
                        newNode.setX_coor(parentNode.getX_coor() - (int)(600 / Math.pow(2, (newNode.getDepth()))));
                        newNode.setY_coor(parentNode.getY_coor() + 150);

                        treePanel.drawTree(root, root);

                        return;
                    }
                }
            }
            else if(value > searchNode.getValue()) { // 오른쪽 자식으로 삽입
                searchNode = searchNode.getRight();
                if(searchNode == null) {
                    if(isFullDepth(parentNode)) {
                        JOptionPane.showMessageDialog(null, "트리의 깊이는 5를 넘을 수 없습니다.", "최대 깊이 초과", JOptionPane.WARNING_MESSAGE);
                    } else {
                        parentNode.setRight(newNode);
                        newNode.setDepth(parentNode.getDepth() + 1);
                        newNode.setX_coor(parentNode.getX_coor() + (int)(600 / Math.pow(2, (newNode.getDepth()))));
                        newNode.setY_coor(parentNode.getY_coor() + 150);

                        treePanel.drawTree(root, root);

                        return;
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "BST 삽입 에러 발생!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    public boolean delete(int value) {
        Node parent_node = root;
        Node search_node = root;
        boolean isLeft = false;

        while(search_node.getValue() != value) {
            parent_node = search_node;
            if(value < search_node.getValue()) {
                isLeft = true;
                search_node = search_node.getLeft();
            } else {
                isLeft = false;
                search_node = search_node.getRight();
            }
            if(search_node == null) {
                return false;
            }
        }

        if(search_node.getLeft() == null && search_node.getRight() == null) { // case1. 자식노드가 없음
            if(search_node == root) { // 삭제하려는 노드가 루트노드
                root = null;
            } else {
                if(isLeft) { // 왼쪽 단말노드
                    parent_node.setLeft(null);
                } else { // 오른쪽 단말노드
                    parent_node.setRight(null);
                }
            }
        } else if(search_node.getLeft() == null) { // case2. 자식노드가 하나
            if(search_node == root) {
                search_node.getRight().setCoordinate(root);
                root = search_node.getRight();
            } else if(isLeft) {
                parent_node.setLeft(search_node.getRight());
                search_node.getRight().setCoordinate(search_node);
            } else {
                parent_node.setRight(search_node.getRight());
                search_node.getRight().setCoordinate(search_node);
            }
        } else if(search_node.getRight() == null) {
            if(search_node == root) {
                root = search_node.getLeft();
            } else if(isLeft) {
                parent_node.setLeft(search_node.getLeft());
                search_node.getLeft().setCoordinate(search_node);
            } else {
                parent_node.setRight(search_node.getRight());
                search_node.getRight().setCoordinate(search_node);
            }
        } else if(search_node.getLeft() != null && search_node.getRight() != null) { // case3. 자식노드가 2개
            Node minNode = getRightMinNode(search_node);
            if(search_node == root) {
                minNode.setCoordinate(root);
                root = minNode;
            } else if(isLeft) {
                parent_node.setLeft(minNode);
                parent_node.getLeft().setCoordinate(search_node);
            } else {
                parent_node.setRight(minNode);
                parent_node.getRight().setCoordinate(search_node);
            }
            minNode.setLeft(search_node.getLeft());
        }

        treePanel.drawTree(root, root);

        return true;
    }

    public Node getRightMinNode(Node n) {
        Node getNode = n.getRight();
        Node parent_node = n;
        while (getNode.getLeft() != null) {
            parent_node = getNode;
            getNode = getNode.getLeft();
        }
        if(getNode != n.getRight()) {
            if(getNode.getRight() == null) {
                parent_node.setLeft(null);
            } else {
                parent_node.setLeft(getNode.getRight());
                getNode.getRight().setCoordinate(getNode);
            }
            getNode.setRight(n.getRight());
        } else {
            if(getNode.getRight() != null) {
                getNode.getRight().setCoordinate(getNode);
            }
        }
        return getNode;
    }
}

class Node { // 노드의 자료구조
    int value; // 키 값
    Node right, left; // 자식 노드
    int depth; // 노드의 레벨 (깊이)
    int x_coor, y_coor; // 노드가 그려질 x좌표, y좌표

    public Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public Node getRight() {
        return right;
    }
    public void setRight(Node right) {
        this.right = right;
    }
    public Node getLeft() {
        return left;
    }
    public void setLeft(Node left) {
        this.left = left;
    }
    public int getX_coor() {
        return x_coor;
    }
    public void setX_coor(int x_coor) {
        this.x_coor = x_coor;
    }
    public int getY_coor() {
        return y_coor;
    }
    public void setY_coor(int y_coor) {
        this.y_coor = y_coor;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCoordinate(Node n) {
        this.setX_coor(n.getX_coor());
        this.setY_coor(n.getY_coor());
        this.setDepth(n.getDepth());
    }
}