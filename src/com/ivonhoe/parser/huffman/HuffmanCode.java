package com.ivonhoe.parser.huffman;

import util.Log;
import util.sort.ElemType;
import util.sort.InsertSort;

import java.io.*;
import java.util.*;

/**
 * http://coolshell.cn/articles/7459.html
 *
 * @author ivonhoe on 15-6-11.
 */
public class HuffmanCode {

    private HashMap<Integer, Integer> mCharCounter = new HashMap<Integer, Integer>();

    private String mTargetPath;
    private String mSourcePath;

    public HashMap<Character, byte[]> loadFile(String filePath) {
        mSourcePath = filePath;
        try {
            return loadInputStream(new FileInputStream(mSourcePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<Character, byte[]> loadInputStream(InputStream inputStream)
            throws IOException {
        Reader reader = new InputStreamReader(inputStream);

        long time = System.currentTimeMillis();

        int temp;
        while ((temp = reader.read()) != -1) {
            // 一次读一个字符
            // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
            // 但如果这两个字符分开显示时，会换两次行。
            // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
            if (((char) temp) != '\r' && ((char) temp) != '\n') {
                int count = 0;
                if (mCharCounter.containsKey(temp)) {
                    count = mCharCounter.get(temp);
                }
                mCharCounter.put(temp, ++count);
            }
        }
        reader.close();
        inputStream.close();

        ElemType[] charElem = new Node[mCharCounter.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : mCharCounter.entrySet()) {
            int key = entry.getKey();
            Node node = new Node();
            node.content = (char) key;
            node.setValue(entry.getValue());
            charElem[i++] = node;
        }

        Node root = createHuffmanTree(charElem);
        HashMap<Character, byte[]> codeMap = new HashMap<Character, byte[]>();
        levelOrderTraverse(root, codeMap);
        Log.e("Load inputStream use time:" + (System.currentTimeMillis() - time) + " ");
        return codeMap;
    }

    public void setTargetFile(String filePath) {
        mTargetPath = filePath;
    }

    public void encode(HashMap<Character, byte[]> characterEncodingMap) {
        if (mSourcePath == null || mTargetPath == null) {
            return;
        }

        byte[] buffer = new byte[3];
        buffer[0] = '1';
        buffer[1] = '1';
        buffer[2] = '1';

        long time = System.currentTimeMillis();
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            OutputStream outputStream = new FileOutputStream(mTargetPath);
            InputStream inputStream = new FileInputStream(mSourcePath);
            Reader reader = new InputStreamReader(inputStream);

            int temp;
            byte[] tempStr = new byte[8];
            int count = 0;
            int a = 0;
            while ((temp = reader.read()) != -1) {
                byte[] encoding = characterEncodingMap.get((char) temp);
                if (encoding != null) {
                    for (byte e : encoding) {
                        tempStr[count] = e;
                        count++;
                        if (count == 8) {
                            byte eight = decodeBinaryString(getString(tempStr));
                            byteOutputStream.write(eight);
                            count = 0;
                            if (a < 2) {
                                Log.e("tempStr:" + getString(tempStr) + ",eight:" + eight);
                                a++;
                            }
                        }
                    }
                }
            }
            byteOutputStream.writeTo(outputStream);
            byteOutputStream.close();
            reader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e("out put time:" + (System.currentTimeMillis() - time));
        }
    }

    public void decode(HashMap<String, Character> encodingMap) {
        long time = System.currentTimeMillis();
        try {
            String file = "";
            // 一次读一个字节
            int tempByte;
            InputStream inputStream = new FileInputStream(mTargetPath);
            while ((tempByte = inputStream.read()) != -1) {
                byte[] a = getBooleanArray((byte) tempByte);
                String b = getString(a);
                file = file + b;
            }
            inputStream.close();

            Log.e("Read file use time:" + (System.currentTimeMillis() - time) + ",file size:" +
                    file.length());
            int index = 0;
            for (int i = 0; i < file.length(); i++) {
                String encoding = file.substring(index, i);

                Character character = encodingMap.get(encoding);
                if (character != null) {
                    // print
/*                    System.out.print(character);
                    if (character == ';') {
                        System.out.println();
                    }*/
                    index = i;
                }
            }
            System.out.println();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e("decode file time:" + (System.currentTimeMillis() - time));
        }
    }

    public static String getString(byte[] bytes) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            b.append(bytes[i]);
        }
        return b.toString();
    }

    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    private Node createHuffmanTree(ElemType[] charElem) {
        // 重新排序
        InsertSort.shellSort(charElem);

        ArrayList<Node> nodeList = new ArrayList<Node>();
        for (ElemType elemType : charElem) {
            Node node = (Node) elemType;
            nodeList.add(node);
        }

        // 生成哈夫曼树
        int i;
        byte zero = 0x00;//'0';
        byte one = 0x01;//'1';
        while (nodeList.size() > 1) {
            Node min1 = nodeList.get(0);
            Node min2 = nodeList.get(1);

            Node newNode = new Node();
            newNode.lChild = min1;
            newNode.rChild = min2;
            newNode.setValue(min1.getValue() + min1.getValue());
            newNode.lChild.updateCode(zero);
            newNode.rChild.updateCode(one);

            nodeList.remove(min1);
            nodeList.remove(min2);

            //新插入
            for (i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.get(i);
                if (node.getValue() >= newNode.getValue()) {
                    break;
                }
            }
            nodeList.add(i, newNode);
        }
        return nodeList.get(0);
    }

    /**
     * 层序遍历
     */
    private void levelOrderTraverse(Node root, HashMap<Character, byte[]> codeMap) {
        codeMap.clear();

        Queue queue = new LinkedList();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node p = (Node) queue.poll();
            if (p.lChild == null && p.rChild == null) {
                byte[] bytes = new byte[p.encoding.size()];
                for (int i = 0; i < p.encoding.size(); i++) {
                    bytes[i] = p.encoding.get(i);
                }
                String s = getString(bytes);
                Log.e("[" + p.content + "," + s + "]");
                codeMap.put(p.content, bytes);
            }
            if (p.lChild != null) {
                queue.offer(p.lChild);
            }

            if (p.rChild != null) {
                queue.offer(p.rChild);
            }
        }
    }

    private ElemType[] getTestCase() {       // for test
        ElemType[] charElem = new Node[7];
        Node node = new Node();
        node.setValue(3);
        node.content = 'b';
        charElem[0] = node;

        node = new Node();
        node.setValue(4);
        node.content = 'e';
        charElem[1] = node;

        node = new Node();
        node.setValue(2);
        node.content = ' ';
        charElem[2] = node;

        node = new Node();
        node.setValue(2);
        node.content = 'o';
        charElem[3] = node;

        node = new Node();
        node.setValue(2);
        node.content = 'p';
        charElem[4] = node;

        node = new Node();
        node.setValue(1);
        node.content = 'r';
        charElem[6] = node;

        node = new Node();
        node.setValue(1);
        node.content = '!';
        charElem[5] = node;

        return charElem;
    }

    /**
     * 二进制字符串转byte
     */
    public static byte decodeBinaryString(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    class Node implements ElemType {

        ArrayList<Byte> encoding = new ArrayList<Byte>();
        char content;
        long value;
        Node lChild;
        Node rChild;

        void updateCode(Byte code) {
            if (lChild != null) {
                lChild.updateCode(code);
            }

            this.encoding.add(0, code);

            if (rChild != null) {
                rChild.updateCode(code);
            }
        }

        @Override
        public long getValue() {
            return value;
        }

        @Override
        public void setValue(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            byte[] bytes = new byte[encoding.size()];
            for (int i = 0; i < encoding.size(); i++) {
                bytes[i] = encoding.get(i);
            }
            String s = null;
            try {
                s = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "[" + content + "," + s + "]";
        }
    }
}
