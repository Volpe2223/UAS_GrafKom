package Engine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;


public class Model extends Object{
    List <Vector3f> normals;
    List <Vector2f> textures;
    List <Face> faces;
    int nbo;
    public Model(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, String filename, boolean lightObject) throws IOException {
        super(shaderModuleDataList, vertices, color, lightObject);
        normals = new ArrayList<>();
        faces = new ArrayList<>();
        textures = new ArrayList<>();
        LoadModel(new File(filename));
        setupVAOVBO();
    }

    public void LoadModel(File f ) throws IOException {
        List <Vector3f> normalV= new ArrayList<>();
        List <Vector3f> verticeV= new ArrayList<>();
        List <Vector2f> textureV= new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        float x,y,z;
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    x = Float.parseFloat(line.split(" ")[1]);
                    y = Float.parseFloat(line.split(" ")[2]);
                    z = Float.parseFloat(line.split(" ")[3]);
//                    System.out.println("Vertices: "+x+" "+y+" "+z);
                    verticeV.add(new Vector3f(x, y, z));
                }
                else if (line.startsWith("vn ")) {
                    x = Float.parseFloat(line.split(" ")[1]);
                    y = Float.parseFloat(line.split(" ")[2]);
                    z = Float.parseFloat(line.split(" ")[3]);
//                    System.out.println("Normals: "+x+" "+y+" "+z);
                    normalV.add(new Vector3f(x, y, z));
                }
                else if (line.startsWith("vt ")) {
                    x = Float.parseFloat(line.split(" ")[1]);
                    y = Float.parseFloat(line.split(" ")[2]);

                    textureV.add(new Vector2f(x, y));
                }

                else if (line.startsWith("f ")) {
                    Vector3f vertexIndices = new Vector3f(
                            Float.parseFloat(line.split(" ")[1].split("/")[0]),
                            Float.parseFloat(line.split(" ")[2].split("/")[0]),
                            Float.parseFloat(line.split(" ")[3].split("/")[0]));

                    Vector3f normalIndices = new Vector3f(
                            Float.parseFloat(line.split(" ")[1].split("/")[2]),
                            Float.parseFloat(line.split(" ")[2].split("/")[2]),
                            Float.parseFloat(line.split(" ")[3].split("/")[2]));
                    if(textureV.size() > 0){
//                        Vector3f TextureIndices = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[1]),
//                                Float.parseFloat(line.split(" ")[2].split("/")[1]),
//                                Float.parseFloat(line.split(" ")[3].split("/")[1]));
                        faces.add(new Face(vertexIndices, normalIndices));
                    }
                    faces.add(new Face(vertexIndices, normalIndices));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        reader.close();

        for (Face face: faces ){
            Vector3f n1 = normalV.get((int)face.normal.x - 1);
//            System.out.println(n1);
            normals.add(n1);
            Vector3f v1 = verticeV.get((int)face.vertex.x - 1);
            vertices.add(v1);
            Vector3f n2 = normalV.get((int)face.normal.y - 1);
            normals.add(n2);
            Vector3f v2 = verticeV.get((int)face.vertex.y - 1);
            vertices.add(v2);
            Vector3f n3 = normalV.get((int)face.normal.z - 1);
            normals.add(n3);
            Vector3f v3 = verticeV.get((int)face.vertex.z - 1);
            vertices.add(v3);
        }
    }
    public Vector3f getPosition() {
        // Mendapatkan rata-rata dari semua verteks objek
        Vector3f position = new Vector3f();
        for (Vector3f vertex : vertices) {
            position.add(vertex);
        }
        position.div(vertices.size());

        return position;
    }


    public void setupVAOVBO(){
        //set vao
        super.setupVAOVBO();
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normals),
                GL_STATIC_DRAW);
    }

    public void drawSetup(Camera camera, Projection projection) {
        super.drawSetup(camera, projection);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);
    }

}
