import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GraphFrame extends JFrame {
    final static int MAX_VERTEX = 10;
    final static String INPUTPANEL_COLOR = "#FFFFFF";
    final static String PRIMPANEL_COLOR = "#81F7D8";
    final static String KRUSKAL_COLOR = "#F6CEF5";
    final static int DIAMETER = 30;
    final static int RADIUS = 15;

    public DrawPanel inputPanel, primPanel, kruskalPanel;
    public JPanel buttonPanel;
    public int vertexCount = 1;
    public int[] parent;

    public JLabel comboLabel1, comboLabel2, textLabel3;
    public JComboBox startVertex, endVertex;
    public JTextField inputWeight;
    public JButton btnMakeEdge, btnMidResult, btnFinalResult;

    public ArrayList<Vertex> allVertex;
    public ArrayList<Edge> allEdge;

    GraphFrame(String title){
        super(title);
        inputPanel = new DrawPanel("INPUT", INPUTPANEL_COLOR);
        buttonPanel = new ButtonPanel();
        primPanel = new DrawPanel("PRIM", PRIMPANEL_COLOR);
        kruskalPanel = new DrawPanel("KRUSKAL", KRUSKAL_COLOR);

        comboLabel1 = new JLabel("VERTEX 1");
        comboLabel2 = new JLabel("VERTEX 2");
        textLabel3 = new JLabel("가중치");
        startVertex = new JComboBox();
        endVertex = new JComboBox();
        inputWeight = new JTextField();
        btnMakeEdge = new JButton("INPUT EDGE");
        btnMidResult = new JButton("중간 결과");
        btnFinalResult = new JButton("최종 결과");

        allVertex = new ArrayList<>();
        allEdge = new ArrayList<>();

        inputPanel.setBounds(20, 50, 500, 500);
        buttonPanel.setBounds(540, 50, 200, 500);
        primPanel.setBounds(760, 50, 500, 500);
        kruskalPanel.setBounds(1280, 50, 500, 500);

        comboLabel1.setPreferredSize(new Dimension(60, 20));
        comboLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        startVertex.setPreferredSize(new Dimension(80, 20));
        comboLabel2.setPreferredSize(new Dimension(60, 20));
        comboLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        endVertex.setPreferredSize(new Dimension(80, 20));
        textLabel3.setPreferredSize(new Dimension(60, 20));
        textLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        inputWeight.setPreferredSize(new Dimension(80, 20));
        btnMakeEdge.setPreferredSize(new Dimension(120, 40));
        btnMidResult.setPreferredSize(new Dimension(120, 40));
        btnFinalResult.setPreferredSize(new Dimension(120, 40));

        inputPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int clickedX = e.getX();
                int clickedY = e.getY();
                int x_start = clickedX - RADIUS;
                int y_start = clickedY - RADIUS;

                if ((5 >= x_start | x_start >= 465) | (5 >= y_start | y_start >= 465)) {
                    JOptionPane.showMessageDialog(null, "정점을 그릴 수 있는 범위가 아닙니다!", "정점 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else if (vertexCount > MAX_VERTEX) {
                    JOptionPane.showMessageDialog(null, "정점의 개수는 최대 10개입니다!", "정점 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else {
                    Vertex vertex = new Vertex(vertexCount, clickedX, clickedY);
                    if (checkVertexOverlap(vertex)) {
                        JOptionPane.showMessageDialog(null, "정점이 겹칩니다!", "정점 생성 불가", JOptionPane.ERROR_MESSAGE);
                    } else {
                        allVertex.add(vertex);
                        startVertex.addItem(vertexCount);
                        endVertex.addItem(vertexCount);
                        inputPanel.drawVertex(vertex);
                        vertexCount += 1;
                    }
                }
            }
        });

        btnMakeEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int startVertexIndex = startVertex.getSelectedIndex();
                int endVertexIndex = endVertex.getSelectedIndex();
                Vertex start = allVertex.get(startVertexIndex);
                Vertex end = allVertex.get(endVertexIndex);

                if (startVertexIndex == endVertexIndex) {
                    JOptionPane.showMessageDialog(null, "자기 자신 연결 불가!", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else if (inputWeight.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "가중치를 입력하세요!", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else {
                    int weight = Integer.parseInt(inputWeight.getText());
                    if (checkEdgeOverlap(start, end)) {
                        JOptionPane.showMessageDialog(null, "이미 존재하는 엣지입니다.", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
                    } else {
                        inputWeight.setText("");
                        inputPanel.makeEdge(start, end, weight);
                    }
                }
            }
        });

        btnMidResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double dLen = Math.ceil((double)allVertex.size() / (double)2);
                int iLen = (int) dLen;
                PrimMST primResult = primAlgo(allVertex.get(0), iLen);
                KruskalMST kruskalResult = kruskalAlgo(iLen);

                primPanel.drawPrim(primResult);
                kruskalPanel.drawKruskal(kruskalResult);

                //primPanel.repaint();
                //kruskalPanel.repaint();
            }
        });

        btnFinalResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimMST primResult = primAlgo(allVertex.get(0), allVertex.size());
                KruskalMST kruskalResult = kruskalAlgo(allVertex.size());

                primPanel.drawPrim(primResult);
                kruskalPanel.drawKruskal(kruskalResult);

                //primPanel.repaint();
                //kruskalPanel.repaint();
            }
        });

        buttonPanel.add(comboLabel1);
        buttonPanel.add(startVertex);
        buttonPanel.add(comboLabel2);
        buttonPanel.add(endVertex);
        buttonPanel.add(textLabel3);
        buttonPanel.add(inputWeight);
        buttonPanel.add(btnMakeEdge);
        buttonPanel.add(btnMidResult);
        buttonPanel.add(btnFinalResult);

        add(inputPanel);
        add(buttonPanel);
        add(primPanel);
        add(kruskalPanel);

        setLayout(null);
        setFocusable(true);
    }

    public boolean checkVertexOverlap(Vertex check) {
        boolean isOverlap = false;
        for (int i = 0; i < allVertex.size(); i++) {
            Vertex temp = allVertex.get(i);
            double deltaX = Math.abs(temp.getxPoint() - check.getxPoint());
            double deltaY = Math.abs(temp.getyPoint() - check.getyPoint());
            double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            if (check.getVertexNum() == temp.getVertexNum()) {
                continue;
            } else {
                if (length < DIAMETER) {
                    isOverlap = true;
                    break;
                }
            }
        }
        return isOverlap;
    }

    public boolean checkEdgeOverlap(Vertex start, Vertex end) {
        boolean isOverlap = false;
        int startVertexNum = start.getVertexNum();
        int targetVertexNum = end.getVertexNum();
        for (Edge temp : allEdge) {
            if ((startVertexNum == temp.getStart().getVertexNum() & targetVertexNum == temp.getEnd().getVertexNum()) || (targetVertexNum == temp.getStart().getVertexNum() & startVertexNum == temp.getEnd().getVertexNum())) {
                isOverlap = true;
                break;
            }
        }
        return isOverlap;
    }

    public PrimMST primAlgo(Vertex start, int length) {
        PrimMST result = new PrimMST();     // S = 최소신장트리에 포함될 정점들의 집합
        ArrayList<Vertex> Q = new ArrayList<>();
        Q.addAll(allVertex);            // 주어진 그래프(정점만 들어있음)
        int[] d = new int[Q.size()];
        Arrays.fill(d, 99999999);
        d[start.getVertexNum() - 1] = 0;    // 정점 Num을 mst에 포함시키는데 드는 비용
        while (Q.size() != 0) {
            Vertex u = deleteMIN(Q, d);
            result.resVertex.add(u);
            if (Q.size() <= allVertex.size() - length) break;
            ArrayList<LinkedVertex> L = new ArrayList<>();
            L.addAll(findLinkVertex(u));
            for (LinkedVertex v : L) {
                if (Q.contains(v.getLinked()) & v.weight < d[v.getLinked().getVertexNum() - 1]) {
                    d[v.getLinked().getVertexNum() - 1] = v.weight;
                }
            }
            result.resEdge.add(findMinEdge(result, d));
        }
        return result;
    }

    public Vertex deleteMIN(ArrayList<Vertex> Q, int[] d) {
        int minDist = d[0];
        int minIndex = 0;
        for(int i = 1; i < d.length; i++) {
            if (d[i] < minDist) {
                minDist = d[i];
                minIndex = i;
            }
        }
        Vertex u = allVertex.get(minIndex);
        Q.remove(u);
        d[minIndex] = 99999999;
        return u;
    }

    public ArrayList<LinkedVertex> findLinkVertex(Vertex vertex) {
        ArrayList<LinkedVertex> L = new ArrayList<>();
        for (Edge temp : allEdge) {
            if (temp.getStart().equals(vertex)) {
                L.add(new LinkedVertex(temp.getEnd(), temp.weight));
            }
            if (temp.getEnd().equals(vertex)) {
                L.add(new LinkedVertex(temp.getStart(), temp.weight));
            }
        }
        return L;
    }

    public Edge findMinEdge(PrimMST r, /*ArrayList<Vertex> Q,*/ int[] d) {
        int minDist = d[0];
        int minIndex = 0;
        for(int i = 1; i < d.length; i++) {
            if (d[i] < minDist) {
                minDist = d[i];
                minIndex = i;
            }
        }
        Vertex target = allVertex.get(minIndex);
        int resultIndex = 0;
        for (Edge temp : allEdge) {
            if (temp.getWeight() == minDist) {
                Vertex start = temp.getStart();
                Vertex end = temp.getEnd();
                if (!r.resEdge.contains(temp)) {
                    /*if (start.equals(target)) {
                        if ()
                    } else if (end.equals(target)) {
                        if ()
                    }*/
                    if (start.equals(target) | end.equals(target))
                        resultIndex = allEdge.indexOf(temp);
                }
            }
        }
        Edge result = allEdge.get(resultIndex);
        return result;
    }

    public KruskalMST kruskalAlgo(int length) {
        KruskalMST result = new KruskalMST();
        parent = new int[allVertex.size()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }
        PriorityQueue<Edge> T = new PriorityQueue<>();
        for (Edge edge : allEdge) {
            T.add(edge);
        }
        while (result.resEdge.size() < length - 1) {
            Edge temp = T.poll();
            Vertex end1 = temp.start;
            Vertex end2 = temp.end;
            if (!isSameParent(end1.vertexNum - 1, end2.vertexNum - 1)) {
                result.resEdge.add(temp);
                if (!result.resVertex.contains(end1)) result.resVertex.add(end1);
                if (!result.resVertex.contains(end2)) result.resVertex.add(end2);
                union(end1.vertexNum - 1, end2.vertexNum - 1);
            }
        }
        return result;
    }

    public void union(int x, int y) {
        x = find(x);
        y = find(y);
        if (x != y) parent[y] = x;
    }

    public int find(int x) {
        if(parent[x] == x) {
            return x;
        }
        return parent[x] = find(parent[x]);
    }

    public boolean isSameParent(int x, int y) {
        x = find(x);
        y = find(y);
        if(x == y) return true;
        else return false;
    }

    class DrawPanel extends JPanel{
        DrawPanel(String title, String color) {
            super();
            this.setBorder(new TitledBorder(new LineBorder(Color.RED, 1), title));
            this.setBackground(Color.decode(color));
            this.setOpaque(true);
            this.setOpaque(true);
        }

        public void drawVertex(Vertex vertex) {
            int xPoint = vertex.getxPoint() - 15;
            int yPoint = vertex.getyPoint() - 15;

            Graphics graphics = getGraphics();
            graphics.setColor(Color.decode("#F7BE81"));
            graphics.fillOval(xPoint, yPoint, DIAMETER, DIAMETER);
            graphics.setColor(Color.BLACK);
            graphics.drawString(Integer.toString(vertex.vertexNum), xPoint + 12, yPoint + 18);
        }

        public void makeEdge(Vertex start, Vertex end, int weight) {
            int startVertexNum = start.getVertexNum();
            int endVertexNum = end.getVertexNum();
            boolean isCollision = false;

            for (Vertex temp : allVertex) {
                if (temp.getVertexNum() == startVertexNum | temp.getVertexNum() == endVertexNum) {
                    continue;
                } else {
                    isCollision = checkCollision(start, end, temp);
                    if(isCollision) break;
                }
            }

            if(isCollision) {
                JOptionPane.showMessageDialog(null, "정점" + start.getVertexNum() + " 과(와) " + "정점" + end.getVertexNum() + " 사이에 겹치는 정점 존재!", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
            } else {
                drawEdge(start, end, weight);
            }
        }

        public boolean checkCollision(Vertex start, Vertex end, Vertex checkVertex) {
            int startX = start.getxPoint();
            int startY = start.getyPoint();
            int endX = end.getxPoint();
            int endY = end.getyPoint();

            double a, b, c;         //ax + by + c = 0
            if (startX == endX) {
                a = 1;
                b = 0;
                c = -startX;
            } else {
                a = (double)(endY - startY) / (double)(endX - startX);
                b = -1;
                c = -startX * a + startY;
            }
            double distance = (Math.abs(a * checkVertex.getxPoint() + b * checkVertex.getyPoint() + c)) / (Math.sqrt(a * a + b * b));
            if (distance <= RADIUS) {
                if (((checkVertex.getxPoint() - DIAMETER < startX & checkVertex.getxPoint() - DIAMETER < endX) | (checkVertex.getxPoint() + DIAMETER > startX & checkVertex.getxPoint() + DIAMETER > endX)) & ((checkVertex.getyPoint() - DIAMETER < startY & checkVertex.getyPoint() - DIAMETER < endY) | (checkVertex.getyPoint() + DIAMETER > startY & checkVertex.getyPoint() + DIAMETER > endY))) {
                    return false;
                } else {
                    return true;
                }
            } else return false;
        }

        public void drawEdge(Vertex start, Vertex end, int weight) {
            int startX = start.getxPoint();
            int startY = start.getyPoint();
            int endX = end.getxPoint();
            int endY = end.getyPoint();
            int deltaX = Math.abs(endX - startX);
            int deltaY = Math.abs(endY - startY);
            double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            int xlen = (int) ((RADIUS * deltaX) / length);
            int ylen = (int) ((RADIUS * deltaY) / length);

            allEdge.add(new Edge(start, end, weight));
            Graphics graphics = getGraphics();
            graphics.setColor(Color.BLACK);
            if (endX > startX) {
                if (endY < startY) {
                    graphics.drawLine(startX + xlen, startY - ylen, endX - xlen, endY + ylen);
                    drawWeight(weight, startX + (int)(0.4 * deltaX) - 10, startY - (int)(0.4 * deltaY));
                } else if (endY == startY) {
                    graphics.drawLine(startX + RADIUS, startY, endX - RADIUS, endY);
                    drawWeight(weight, startX + (int)(0.4 * deltaX), startY + 10);
                } else {
                    graphics.drawLine(startX + xlen, startY + ylen, endX - xlen, endY - ylen);
                    drawWeight(weight, startX + (int)(0.4 * deltaX) + 5, startY + (int)(0.4 * deltaY));
                }
            } else if (endX == startX) {
                if (endY < startY) {
                    graphics.drawLine(startX, startY - RADIUS, endX, endY + RADIUS);
                    drawWeight(weight, startX + 5, startY - (int)(0.4 * deltaY));
                } else {
                    graphics.drawLine(startX, startY + RADIUS, endX, endY - RADIUS);
                    drawWeight(weight, startX + 5, startY + (int)(0.4 * deltaY));
                }
            } else {
                if (endY < startY) {
                    graphics.drawLine(startX - xlen, startY - ylen, endX + xlen, endY + ylen);
                    drawWeight(weight, startX - (int)(0.4 * deltaX) + 5, startY - (int)(0.4 * deltaY));
                } else if (endY == startY) {
                    graphics.drawLine(startX - RADIUS, startY, endX + RADIUS, endY);
                    drawWeight(weight, startX - (int)(0.4 * deltaX), startY - 5);
                } else {
                    graphics.drawLine(startX - xlen, startY + ylen, endX + xlen, endY - ylen);
                    drawWeight(weight, startX - (int)(0.4 * deltaX) + 10, startY + (int)(0.4 * deltaY));
                }
            }
        }

        public void drawEdge(Edge edge) {
            Vertex start = edge.getStart();
            Vertex end = edge.getEnd();
            int weight = edge.getWeight();
            int startX = start.getxPoint();
            int startY = start.getyPoint();
            int endX = end.getxPoint();
            int endY = end.getyPoint();
            int deltaX = Math.abs(endX - startX);
            int deltaY = Math.abs(endY - startY);
            double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            int xlen = (int) ((RADIUS * deltaX) / length);
            int ylen = (int) ((RADIUS * deltaY) / length);

            Graphics graphics = getGraphics();
            graphics.setColor(Color.BLACK);
            if (endX > startX) {
                if (endY < startY) {
                    graphics.drawLine(startX + xlen, startY - ylen, endX - xlen, endY + ylen);
                    drawWeight(weight, startX + (int)(0.4 * deltaX) - 10, startY - (int)(0.4 * deltaY));
                } else if (endY == startY) {
                    graphics.drawLine(startX + RADIUS, startY, endX - RADIUS, endY);
                    drawWeight(weight, startX + (int)(0.4 * deltaX), startY + 10);
                } else {
                    graphics.drawLine(startX + xlen, startY + ylen, endX - xlen, endY - ylen);
                    drawWeight(weight, startX + (int)(0.4 * deltaX) + 5, startY + (int)(0.4 * deltaY));
                }
            } else if (endX == startX) {
                if (endY < startY) {
                    graphics.drawLine(startX, startY - RADIUS, endX, endY + RADIUS);
                    drawWeight(weight, startX + 5, startY - (int)(0.4 * deltaY));
                } else {
                    graphics.drawLine(startX, startY + RADIUS, endX, endY - RADIUS);
                    drawWeight(weight, startX + 5, startY + (int)(0.4 * deltaY));
                }
            } else {
                if (endY < startY) {
                    graphics.drawLine(startX - xlen, startY - ylen, endX + xlen, endY + ylen);
                    drawWeight(weight, startX - (int)(0.4 * deltaX) + 5, startY - (int)(0.4 * deltaY));
                } else if (endY == startY) {
                    graphics.drawLine(startX - RADIUS, startY, endX + RADIUS, endY);
                    drawWeight(weight, startX - (int)(0.4 * deltaX), startY - 5);
                } else {
                    graphics.drawLine(startX - xlen, startY + ylen, endX + xlen, endY - ylen);
                    drawWeight(weight, startX - (int)(0.4 * deltaX) + 10, startY + (int)(0.4 * deltaY));
                }
            }
        }

        public void drawWeight(int weight, int x, int y) {
            String Weight = Integer.toString(weight);
            Graphics graphics = getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.drawString(Weight, x, y);
        }

        public void drawPrim(PrimMST primMST) {
            for (Vertex vertex : primMST.resVertex) {
                primPanel.drawVertex(vertex);
            }
            for (Edge edge : primMST.resEdge) {
                primPanel.drawEdge(edge);
            }
        }

        public void drawKruskal(KruskalMST kruskalMST) {
            for (Vertex vertex : kruskalMST.resVertex) {
                kruskalPanel.drawVertex(vertex);
            }
            for (Edge edge : kruskalMST.resEdge) {
                kruskalPanel.drawEdge(edge);
            }
        }
    }

    class ButtonPanel extends JPanel {
        ButtonPanel() {
            super();
            this.setBorder(new LineBorder(Color.RED, 1));
            this.setBackground(Color.LIGHT_GRAY);
            this.setOpaque(true);
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
        }
    }

    class Vertex {
        public int vertexNum, xPoint, yPoint;
        public int distance;

        Vertex(int vertexNum, int xPoint, int yPoint) {
            this.vertexNum = vertexNum;
            this.xPoint = xPoint;
            this.yPoint = yPoint;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
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
    }   // 정점 클래스

    class Edge implements Comparable<Edge> {
        public Vertex start, end;
        public int weight;

        Edge(Vertex start, Vertex end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        public Vertex getStart() {
            return start;
        }

        public void setStart(Vertex start) {
            this.start = start;
        }

        public Vertex getEnd() {
            return end;
        }

        public void setEnd(Vertex end) {
            this.end = end;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            return this.weight - o.weight;
        }
    }   // 엣지 클래스

    class LinkedVertex {    // 인접 정점 리스트
        public Vertex linked;
        public int weight;

        LinkedVertex(Vertex linked, int weight) {
            this.linked = linked;
            this.weight = weight;
        }

        public Vertex getLinked() {
            return linked;
        }

        public void setLinked(Vertex linked) {
            this.linked = linked;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    class PrimMST {         // 그려낼 트리 자료구조
        public List<Vertex> resVertex;
        public List<Edge> resEdge;

        PrimMST() {
            this.resVertex = new ArrayList<>();
            this.resEdge = new ArrayList<>();
        }
    }

    class KruskalMST {
        public List<Vertex> resVertex;
        public List<Edge> resEdge;

        KruskalMST() {
            this.resVertex = new ArrayList<>();
            this.resEdge = new ArrayList<>();
        }
    }

    /*class LinkedEdge implements Comparable<LinkedEdge> {    // 인접 정점을 잇는 간선 자료구조 -> 우선순위 큐
        public Vertex linkVertex;
        public int weight;

        LinkedEdge(Vertex linkVertex, int weight) {
            this.linkVertex = linkVertex;
            this.weight = weight;
        }

        public Vertex getLinkVertex() {
            return this.linkVertex;
        }

        public void setLinkVertex(Vertex linkVertex) {
            this.linkVertex = linkVertex;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public int compareTo(LinkedEdge o) {
            return this.weight - o.weight;
        }
    }*/

    public static void main(String[] args) {
        JFrame frame = new GraphFrame("2016010887이수호");
        frame.setSize(1810, 630);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
// TODO: 2020-12-01 중간 결과 출력 수정, 여러 입력 해보기
