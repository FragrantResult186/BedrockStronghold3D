package fragrant.viewer.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.common.nio.Buffers;
import kaptainwutax.featureutils.structure.generator.StrongholdGenerator;
import kaptainwutax.featureutils.structure.generator.piece.StructurePiece;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.util.BlockBox;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class StrongholdRenderer {
    private StrongholdGenerator generator;
    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer colorBuffer;
    private int vertexCount = 0;
    private float centerX = 0f;
    private float centerY = 0f;
    private float centerZ = 0f;

    public void generateStronghold(long seed, int chunkX, int chunkZ) {
        generator = new StrongholdGenerator(MCVersion.v1_16);
        generator.generate((int)seed, chunkX, chunkZ);

        centerX = chunkX * 16;
        centerY = 0;
        centerZ = chunkZ * 16;

        createVertexData();
    }

    public void initGL(GL2 gl) {
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        float[] lightPosition = {1000.0f, 1000.0f, 1000.0f, 1.0f};
        float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);

        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 50.0f);
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
    }

    public void render(GL2 gl) {
        drawAxes(gl);
        drawStructure(gl);
        drawGrid(gl);
    }

    private void drawStructure(GL2 gl) {
        if (vertexBuffer != null && normalBuffer != null && colorBuffer != null) {
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

            gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertexBuffer);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, normalBuffer);
            gl.glColorPointer(3, GL2.GL_FLOAT, 0, colorBuffer);

            gl.glDrawArrays(GL2.GL_QUADS, 0, vertexCount);

            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        }
    }

    public void reshape(GL2 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float aspect = (float)width / height;
        float fovY = 45.0f;
        float zNear = 0.1f;
        float zFar = 2000.0f;
        float fH = (float)(Math.tan(Math.toRadians(fovY / 2)) * zNear);
        float fW = fH * aspect;
        gl.glFrustum(-fW, fW, -fH, fH, zNear, zFar);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GL2 gl) {
    }

    private void createVertexData() {
        List<Float> vertices = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> colors = new ArrayList<>();

        float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

        for (StructurePiece<?> piece : generator.pieceList) {
            BlockBox box = piece.getBoundingBox();
            minY = Math.min(minY, box.minY);
            maxY = Math.max(maxY, box.maxY);

            addCuboid(vertices, normals, colors,
                    box.minX, box.minY, box.minZ,
                    box.maxX - box.minX,
                    box.maxY - box.minY,
                    box.maxZ - box.minZ);
        }

        vertexBuffer = Buffers.newDirectFloatBuffer(vertices.size());
        normalBuffer = Buffers.newDirectFloatBuffer(normals.size());
        colorBuffer = Buffers.newDirectFloatBuffer(colors.size());

        for (int i = 0; i < vertices.size(); i++) {
            vertexBuffer.put(vertices.get(i));
            normalBuffer.put(normals.get(i));
            colorBuffer.put(colors.get(i));
        }

        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer.rewind();
        vertexCount = vertices.size() / 3;
    }

    private void addCuboid(List<Float> vertices, List<Float> normals, List<Float> colors,
                           float x, float y, float z,
                           float width, float height, float depth) {
        addQuad(vertices, normals,
                x, y, z + depth,
                x + width, y, z + depth,
                x + width, y + height, z + depth,
                x, y + height, z + depth,
                0, 0, 1);

        addQuad(vertices, normals,
                x + width, y, z,
                x, y, z,
                x, y + height, z,
                x + width, y + height, z,
                0, 0, -1);

        addQuad(vertices, normals,
                x + width, y, z + depth,
                x + width, y, z,
                x + width, y + height, z,
                x + width, y + height, z + depth,
                1, 0, 0);

        addQuad(vertices, normals,
                x, y, z,
                x, y, z + depth,
                x, y + height, z + depth,
                x, y + height, z,
                -1, 0, 0);

        addQuad(vertices, normals,
                x, y + height, z + depth,
                x + width, y + height, z + depth,
                x + width, y + height, z,
                x, y + height, z,
                0, 1, 0);

        addQuad(vertices, normals,
                x, y, z,
                x + width, y, z,
                x + width, y, z + depth,
                x, y, z + depth,
                0, -1, 0);

        for (int i = 0; i < 24; i++) {
            colors.add(0.7f);
            colors.add(0.7f);
            colors.add(0.7f);
        }
    }

    private void addQuad(List<Float> vertices, List<Float> normals,
                         float x1, float y1, float z1,
                         float x2, float y2, float z2,
                         float x3, float y3, float z3,
                         float x4, float y4, float z4,
                         float nx, float ny, float nz) {
        vertices.add(x1); vertices.add(y1); vertices.add(z1);
        vertices.add(x2); vertices.add(y2); vertices.add(z2);
        vertices.add(x3); vertices.add(y3); vertices.add(z3);
        vertices.add(x4); vertices.add(y4); vertices.add(z4);

        for (int i = 0; i < 4; i++) {
            normals.add(nx);
            normals.add(ny);
            normals.add(nz);
        }
    }

    private void drawAxes(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBegin(GL2.GL_LINES);

        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(centerX - 50, centerY, centerZ);
        gl.glVertex3f(centerX + 50, centerY, centerZ);

        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(centerX, centerY - 50, centerZ);
        gl.glVertex3f(centerX, centerY + 50, centerZ);

        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(centerX, centerY, centerZ - 50);
        gl.glVertex3f(centerX, centerY, centerZ + 50);

        gl.glEnd();
        gl.glEnable(GL2.GL_LIGHTING);
    }

    private void drawGrid(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor3f(0.3f, 0.3f, 0.3f);

        int gridSize = 100;
        int step = 16;

        gl.glBegin(GL2.GL_LINES);

        for (int x = -gridSize; x <= gridSize; x += step) {
            gl.glVertex3f(centerX + x, centerY, centerZ - gridSize);
            gl.glVertex3f(centerX + x, centerY, centerZ + gridSize);
        }

        for (int z = -gridSize; z <= gridSize; z += step) {
            gl.glVertex3f(centerX - gridSize, centerY, centerZ + z);
            gl.glVertex3f(centerX + gridSize, centerY, centerZ + z);
        }

        gl.glEnd();
        gl.glEnable(GL2.GL_LIGHTING);
    }
}
