package fragrant.viewer.graphics;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.*;
import java.awt.Point;

public class GLPanel extends GLJPanel implements GLEventListener {
    private final Camera camera;
    private final StrongholdRenderer renderer;
    private Point prevMouse = null;
    private int prevMouseButton = -1;

    public GLPanel(Camera camera, StrongholdRenderer renderer) {
        this.camera = camera;
        this.renderer = renderer;

        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(glProfile);
        capabilities.setSampleBuffers(true);
        capabilities.setNumSamples(4);

        addGLEventListener(this);
        setupControls();

        FPSAnimator animator = new FPSAnimator(this, 120);
        animator.start();
    }

    private void setupControls() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (prevMouse != null) {
                    int dx = e.getX() - prevMouse.x;
                    int dy = e.getY() - prevMouse.y;

                    if (prevMouseButton == MouseEvent.BUTTON1) {
                        camera.adjustRotation(dx, dy);
                    } else if (prevMouseButton == MouseEvent.BUTTON3) {
                        camera.moveRelative(-dx * 0.1f, 0, -dy * 0.1f);
                    }
                }
                prevMouse = e.getPoint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevMouse = e.getPoint();
                prevMouseButton = e.getButton();
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                prevMouse = null;
                prevMouseButton = -1;
            }
        });

        addMouseWheelListener(e -> {
            camera.adjustDistance(e.getWheelRotation() * 5.0f);
            repaint();
        });

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: camera.setMovingForward(true); break;
                    case KeyEvent.VK_S: camera.setMovingBack(true); break;
                    case KeyEvent.VK_A: camera.setMovingLeft(true); break;
                    case KeyEvent.VK_D: camera.setMovingRight(true); break;
                    case KeyEvent.VK_SPACE: camera.setMovingUp(true); break;
                    case KeyEvent.VK_SHIFT: camera.setMovingDown(true); break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: camera.setMovingForward(false); break;
                    case KeyEvent.VK_S: camera.setMovingBack(false); break;
                    case KeyEvent.VK_A: camera.setMovingLeft(false); break;
                    case KeyEvent.VK_D: camera.setMovingRight(false); break;
                    case KeyEvent.VK_SPACE: camera.setMovingUp(false); break;
                    case KeyEvent.VK_SHIFT: camera.setMovingDown(false); break;
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                camera.setMovingForward(false);
                camera.setMovingBack(false);
                camera.setMovingLeft(false);
                camera.setMovingRight(false);
                camera.setMovingUp(false);
                camera.setMovingDown(false);
            }
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        renderer.initGL(gl);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        camera.update();

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        camera.applyViewTransform(gl);
        renderer.render(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        renderer.reshape(gl, width, height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        renderer.dispose(drawable.getGL().getGL2());
    }
}