package me.val.plugins;

import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.common.util.NamedList;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.util.IOUtils;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.ContentStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

//curl -X POST -H "Content-Type: image/png" --data-binary "@0070039.png" http://localhost:8983/solr/new_core123/custom
public class CustomRequestHandler extends RequestHandlerBase {
    private static boolean initialized = false;
    private static CNN_Model model;

    @Override
    public void init(NamedList args) {
        super.init(args);

        if (!initialized) {
            // Esegui l'azione una sola volta qui
            initialized = true;
            try {
                String savedModelPath = extractModelFromResource();
                model = new CNN_Model(savedModelPath);
            } catch (IOException e) {
                System.out.println(e);
            } catch (URISyntaxException e) {
                System.out.println(e);
            }

            //
        }
    }

    public static String extractModelFromResource() throws IOException, URISyntaxException {
        String resourceZipPath = "saved_model.zip"; // Path to the zip file in "resources" folder
        System.out.println("[APACHE SOLR PLUGIN] temp folder is "+ System.getProperty("java.io.tmpdir"));
        String destinationFolderPath = System.getProperty("java.io.tmpdir") + "\\saved_model"; // Destination folder
                                                                                               // path

        URL resourceZipUrl = CustomRequestHandler.class.getClassLoader().getResource(resourceZipPath);
        if (resourceZipUrl == null) {
            throw new IllegalArgumentException("Resource zip file not found");
        }

        // Create the destination folder if it doesn't exist
        File destinationFolder = new File(destinationFolderPath);
        if (destinationFolder.exists()) {
            System.out.println("[APACHE SOLR PLUGIN] FOLDER ALREADY EXISTS, SKIPPING UNZIP..");
            return destinationFolderPath;
        }
        destinationFolder.mkdirs();


        // Extract the zip file
        try (ZipInputStream zipInputStream = new ZipInputStream(
                CustomRequestHandler.class.getClassLoader().getResourceAsStream(resourceZipPath))) {

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                String entryPath = destinationFolderPath + File.separator + entryName;

                if (entry.isDirectory()) {
                    File dirToCreate = new File(entryPath);
                    dirToCreate.mkdirs();
                } else {
                    try (FileOutputStream outputStream = new FileOutputStream(entryPath)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }

        System.out.println("[APACHE SOLR PLUGIN] Model extraction completed");
        return destinationFolderPath;
    }

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
        InputStream imageStream = null;
        try {
            // Extract the image from the request
            Iterable<ContentStream> contentStreams = req.getContentStreams();
            if (contentStreams != null) {
                for (ContentStream contentStream : contentStreams) {
                    imageStream = contentStream.getStream();

                    // Load the image using an image processing library
                    BufferedImage image = ImageIO.read(imageStream);

                    // Get the image resolution (width and height)
                    int width = image.getWidth();
                    int height = image.getHeight();

                    // Create a Solr document to hold the response
                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("image_width", width);
                    doc.addField("image_height", height);

                    float[] result = model.calculateFeatureVector(image);

                    for (float num : result) {
                        System.out.println(num);
                    }

                    // Convert SolrInputDocument to Map<String, Object>
                    Map<String, Object> docMap = new HashMap<>();
                    docMap.putAll(doc);

                    // Add the document to the response
                    String arrayAsString = Arrays.toString(result);
                    rsp.add("image_resolution_x", docMap);
                    rsp.add("feature_vector", arrayAsString);
                }
            }
        } catch (IOException e) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Error processing the image", e);
        } finally {
            IOUtils.closeWhileHandlingException(imageStream);
        }
    }

    @Override
    public String getDescription() {
        return "Custom Request Handler";
    }

    @Override
    public Name getPermissionName(AuthorizationContext context) {
        // Return the permission name based on the authorization context
        return null;
    }
}
