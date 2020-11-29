import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class Vertex {
    public int vertexNum, xPoint, yPoint;

    public Vertex(int vertexNum, int xPoint, int yPoint) {
        this.vertexNum = vertexNum;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public void setVertexNum(int vertexNum) {
        this.vertexNum = vertexNum;
    }

    public int getxPoint() {
        return xPoint;
    }

    public void setxPoint(int xPoint) {
        this.xPoint = xPoint;
    }

    public int getyPoint() {
        return yPoint;
    }

    public void setyPoint(int yPoint) {
        this.yPoint = yPoint;
    }
}

class Edge {
    public Vertex start, target;
    public int weight;
    public boolean isOpen;

    public Edge(Vertex start, Vertex target, int weight, boolean isOpen) {
        this.start = start;
        this.target = target;
        this.weight = weight;
        this.isOpen = isOpen;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}

public class GraphFrame extends JFrame {
    final int MAX_VERTEX = 10;

    public DrawPanel inputPanel, primPanel, kruskalPanel;
    public ButtonPanel buttonPanel;
    public int vertexCount = 1;

    public JLabel comboLabel1, comboLabel2, textLabel3;
    public JComboBox startVertex, endVertex;
    public JTextField inputWeight;
    public JButton btnEdge, btnMidResult, btnFinalResult;

    public ArrayList<Vertex> allVertex;
    public ArrayList<Edge> allEdge;

    GraphFrame(String title){
        super(title);
        inputPanel = new DrawPanel();
        primPanel = new DrawPanel();
        kruskalPanel = new DrawPanel();
        buttonPanel = new ButtonPanel();

        comboLabel1 = new JLabel("VERTEX 1");
        comboLabel2 = new JLabel("VERTEX 2");
        textLabel3 = new JLabel("가중치");
        startVertex = new JComboBox();
        endVertex = new JComboBox();
        inputWeight = new JTextField();
        btnEdge = new JButton("INPUT EDGE");
        btnMidResult = new JButton("중간 결과");
        btnFinalResult = new JButton("최종 결과");

        allVertex = new ArrayList<Vertex>();
        allEdge = new ArrayList<Edge>();

        inputPanel.setBorder(new TitledBorder(new LineBorder(Color.RED, 1), "INPUT"));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setOpaque(true);
        buttonPanel.setBorder(new TitledBorder(new LineBorder(Color.RED, 1)));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setOpaque(true);
        primPanel.setBorder(new TitledBorder(new LineBorder(Color.RED, 1), "PRIM"));
        primPanel.setBackground(new Color(155, 229, 200));
        primPanel.setOpaque(true);
        kruskalPanel.setBorder(new TitledBorder(new LineBorder(Color.RED, 1), "KRUSKAL"));
        kruskalPanel.setBackground(new Color(236, 206, 245));
        kruskalPanel.setOpaque(true);

        inputPanel.setBounds(20, 50, 500, 500);
        buttonPanel.setBounds(540, 50, 200, 500);
        primPanel.setBounds(760, 50, 500, 500);
        kruskalPanel.setBounds(1280, 50, 500, 500);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
        comboLabel1.setPreferredSize(new Dimension(60, 20));
        comboLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        startVertex.setPreferredSize(new Dimension(80, 20));
        comboLabel2.setPreferredSize(new Dimension(60, 20));
        comboLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        endVertex.setPreferredSize(new Dimension(80, 20));
        textLabel3.setPreferredSize(new Dimension(60, 20));
        textLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        inputWeight.setPreferredSize(new Dimension(80, 20));
        btnEdge.setPreferredSize(new Dimension(120, 40));
        btnMidResult.setPreferredSize(new Dimension(120, 40));
        btnFinalResult.setPreferredSize(new Dimension(120, 40));

        inputPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int clickedX = e.getX();
                int clickedY = e.getY();
                int x_start = clickedX - 15;
                int y_start = clickedY - 15;

                if ((5 >= x_start | x_start >= 465) | (5 >= y_start | y_start >= 465)) {
                    JOptionPane.showMessageDialog(null, "입력 패널 범위 밖입니다.!", "노드 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else if (vertexCount > MAX_VERTEX) {
                    JOptionPane.showMessageDialog(null, "노드의 개수는 최대 10개입니다.!", "노드 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else {
                    Vertex vertex = new Vertex(vertexCount, clickedX, clickedY);
                    allVertex.add(vertex);
                    startVertex.addItem(vertexCount);
                    endVertex.addItem(vertexCount);
                    inputPanel.drawVertex(x_start, y_start);
                    vertexCount += 1;
                }
            }
        });

        buttonPanel.add(comboLabel1);
        buttonPanel.add(startVertex);
        buttonPanel.add(comboLabel2);
        buttonPanel.add(endVertex);
        buttonPanel.add(textLabel3);
        buttonPanel.add(inputWeight);
        buttonPanel.add(btnEdge);
        buttonPanel.add(btnMidResult);
        buttonPanel.add(btnFinalResult);

        add(inputPanel);
        add(buttonPanel);
        add(primPanel);
        add(kruskalPanel);

        setLayout(null);
        setFocusable(true);
    }

    class DrawPanel extends JPanel{
        DrawPanel() {
            super();
            this.setBorder(new LineBorder(Color.RED, 2));
        }

        public void drawVertex(int xPoint, int yPoint) {
            Graphics graphics = getGraphics();
            graphics.setColor(Color.decode("#F7BE81"));
            graphics.fillOval(xPoint, yPoint, 30, 30);
            graphics.setColor(Color.BLACK);
            graphics.drawString(Integer.toString(vertexCount), xPoint + 12, yPoint + 18);
        }
    }

    class ButtonPanel extends JPanel {
        ButtonPanel() {
            super();
            this.setBorder(new LineBorder(Color.RED, 2));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new GraphFrame("2016010887이수호");
        frame.setSize(1810, 630);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

// TODO: 2020-11-29 정점과 간선은 겹치지 않게 그리기, 프림 알고리즘 구현, 크루스칼 알고리즘 구현