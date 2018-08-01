package mx.gob.admic.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;
import mx.gob.admic.R;
import mx.gob.admic.api.RegistroModificarPerfil;
import mx.gob.admic.api.Response;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.model.DatosModificarPerfil;
import mx.gob.admic.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Autor: Uriel Infante
 * Fragment de datos complementarios del usuario, esta interfaz solicita información obligatoria
 * al usuario, y es llamada cuando se va a crear un nuevo usuario, sin importar su forma de logueo.
 * Fecha: 02/05/2016
 */
public class EditarDatosFragment extends CustomFragment {
    private static final String EMAIL = "email";
    private static final String NOMBRE = "nombre";
    private static final String AP_PATERNO = "ap_paterno";
    private static final String RUTA_IMAGEN = "ruta_imagen";

    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FROM_GALLERY = 102;
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_CODE = 2;

    private Button btnContinuar;
    private CircleImageView imgPerfil;

    private MaterialSpinner spnNivelEstudios;
    private ProgressDialog progressDialog;

    private EditText editTextCalle;
    private EditText editTextColonia;
    private EditText editTextCiudad;
    private EditText editTextEmpresa;
    private EditText editTextActividad;
    private EditText editTextNumEmpleados;
    private EditText editTextRFC;
    private EditText editTextAntiguedad;

    private RegistroModificarPerfil registroModificarPerfilAPI;

    private String[] siNo = {"Sí", "No"};
    private String[] nivelesEstudio = {"Primaria", "Secundaria", "Preparatoria", "TSU", "Universidad", "Maestría", "Doctorado", "Otro"};
    public static Activity thisActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        registroModificarPerfilAPI = retrofit.create(RegistroModificarPerfil.class);
        thisActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_datos, parent, false);

        btnContinuar = (Button) v.findViewById(R.id.btn_continuar);
        spnNivelEstudios = (MaterialSpinner) v.findViewById(R.id.spn_nivel_estudios);
        imgPerfil = (CircleImageView) v.findViewById(R.id.imagen_usuario);
        editTextCalle = (EditText) v.findViewById(R.id.et_calle);
        editTextColonia = (EditText) v.findViewById(R.id.et_colonia);
        editTextCiudad = (EditText) v.findViewById(R.id.et_ciudad);
        editTextEmpresa = (EditText) v.findViewById(R.id.et_empresa);
        editTextActividad = (EditText) v.findViewById(R.id.et_actividad);
        editTextNumEmpleados = (EditText) v.findViewById(R.id.et_numero_empleados);
        editTextRFC = (EditText) v.findViewById(R.id.et_rfc);
        editTextAntiguedad = (EditText) v.findViewById(R.id.et_antiguedad);

        ArrayAdapter<String> siNoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, siNo);
        ArrayAdapter<String> nivelEstudioAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nivelesEstudio);

        siNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nivelEstudioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNivelEstudios.setAdapter(nivelEstudioAdapter);

        imgPerfil.setOnClickListener((View) -> { selectImage(); });

        btnContinuar.setOnClickListener((View -> {
            botonGuardar();
        }));

        Picasso.with(getActivity()).load(Sesion.getUsuario().getDatosUsuario().getRutaImagen()).into(imgPerfil);

        cargarDatos();

        return v;
    }

    private void selectImage() {
        final CharSequence[] items = {"Tomar una foto", "Escoger de tu galería"};

        //Se construye el dialog que muestra las opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Añadir imagen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Tomar una foto")) {
                    checkCameraPermission();
                } else if (items[item].equals("Escoger de tu galería")) {
                    checkStoragePermission();
                }
            }
        });

        //Se el diálog
        builder.show();
    }


    /**
     * Método que checa el permiso de la cámara para inicializar la ventana.
     */
    public void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                startCamera();
            }
        } else {
            startCamera();
        }
    }


    /**
     * Función para revisar el permiso de amlacenamiento externo, permite ver las imágenes de la
     * galería.
     */
    public void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            } else {
                startGallery();
            }
        } else {
            startGallery();
        }
    }


    /**
     * Metodo que lanza el intent con la actividad de la cámara (Se toma la foto y existe la opción
     * de aceptar o cancelar.
     */
    public void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
    }


    /**
     * Función que lanza el selector de imágenes de la galería, debe haberse dado el permiso
     * READ_EXTERNAL_STORAGE antes para abrir.
     */
    public void startGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Selecciona una imagen"),
                SELECT_FROM_GALLERY);
    }

    /**
     * Callback ejecutado cuando se asigna un permiso, ejecuta la función del permiso una vez que sea
     * aceptado.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {

                    Snackbar.make(getView(), "Permiso denegado, no se puede acceder a la cámara", Snackbar.LENGTH_LONG).show();
                }
                return;
            case READ_EXTERNAL_STORAGE_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                } else {
                    Snackbar.make(getView(), "Permiso denegado, no se puede acceder a los archivos", Snackbar.LENGTH_LONG).show();
                }
                return;

        }
    }

    /**
     * Función para reducir el tamaño de un bitmap.
     *
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    /**
     * Función ejecutada cuando se regresa de una actividad que manda respuesta, en este caso sirve
     * para cargar la imagen devuelta de las activities de cámara y galería.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // Display image received on the view
                Bundle b = data.getExtras(); // Kept as a Bundle to check for other things in my actual code
                Bitmap pic = (Bitmap) b.get("data");

                if (pic != null) { // Display your image in an ImageView in your layout (if you want to test it)
                    imgPerfil.setImageBitmap(pic);
                    imgPerfil.invalidate();
                }
            } else if (requestCode == SELECT_FROM_GALLERY) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = getResizedBitmap(BitmapFactory.decodeFile(tempPath, btmapOptions), 300);
                imgPerfil.setImageBitmap(bm);
            }
        }
    }


    /**
     * Función que obtiene el path de un bitmap para cargarlo en el imageView.
     *
     * @param uri
     * @param activity
     * @return
     */
    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**
     * Se transforma el contenido de un ImageView en un String base64 para enviar al servidor.
     *
     * @param imageView
     * @return
     */
    public String getBase64(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bb = bos.toByteArray();
        String image = Base64.encodeToString(bb, 0);

        return image;
    }

    //Limpia el arreglo estatico de idiomas
    @Override
    public void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    //Método para el botón de guardar
    public void botonGuardar() {
        if (datosCompletos()) {
            AlertDialog.Builder $mensajeConfirmacion = new AlertDialog.Builder(getContext());
            $mensajeConfirmacion.setTitle("Confirmación");

            $mensajeConfirmacion.setMessage("¿Deseas actualizar tu información?");

            $mensajeConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> registrarDatos());

            $mensajeConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

            $mensajeConfirmacion.show();

        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Ingresa todas las opciones");
            dialog.create();
            dialog.show();
        }
    }

    //Registra los datos en BD
    private void registrarDatos() {
        //Datos a registrar
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Registrando datos", "Por favor espere", true, false);
        AlertDialog.Builder mensaje = new AlertDialog.Builder(getContext());

        String apiToken = Sesion.getUsuario().getApiToken();
        int nivelEstudios = spnNivelEstudios.getSelectedItemPosition();
        String rutaImagen = "data:image/jpeg;base64," + getBase64(imgPerfil);
        String calle = editTextCalle.getText().toString();
        String colonia = editTextColonia.getText().toString();
        String ciudad = editTextCiudad.getText().toString();
        String empresa = editTextEmpresa.getText().toString();
        String actividad = editTextActividad.getText().toString();
        int numeroEmpleados = Integer.valueOf(editTextNumEmpleados.getText().toString());
        String rfc = editTextRFC.getText().toString();
        int antiguedad = Integer.valueOf(editTextAntiguedad.getText().toString());

        DatosModificarPerfil datosModificarPerfil = new DatosModificarPerfil(
                apiToken, rutaImagen, nivelEstudios, calle, colonia, ciudad, empresa, actividad, numeroEmpleados, rfc, antiguedad);

        Call<Response<Boolean>> callRegistrar = registroModificarPerfilAPI.postModificarPerfil(datosModificarPerfil);

        callRegistrar.enqueue(new Callback<Response<Boolean>>() {
            @Override
            public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                progressDialog.dismiss();
                mensaje.setTitle("Datos registrados");
                mensaje.setMessage("Datos registrados con éxito");
                mensaje.show();
            }

            @Override
            public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                progressDialog.dismiss();
                mensaje.setTitle("Error");
                mensaje.setMessage("Error al registrar los datos");
                mensaje.show();
            }
        });
    }

    //Cuando abre el fragment se ejecuta
    private void cargarDatos() {
        String apiToken = Sesion.getUsuario().getApiToken();
        Call<Response<DatosModificarPerfil>> cargarDatos = registroModificarPerfilAPI.getModificarPerfil(apiToken);

        cargarDatos.enqueue(new Callback<Response<DatosModificarPerfil>>() {
            @Override
            public void onResponse(Call<Response<DatosModificarPerfil>> call, retrofit2.Response<Response<DatosModificarPerfil>> response) {
                DatosModificarPerfil datosModificarPerfil = response.body().data;
                asignarDatos(datosModificarPerfil);
            }

            @Override
            public void onFailure(Call<Response<DatosModificarPerfil>> call, Throwable t) {
                Snackbar.make(getView(), "Error al cargar los datos", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    //Métodos para verificar que ingrese todos los datos
    private boolean datosCompletos() {
        if (confirmoNivelEstudios()) {
            if (!editTextCalle.getText().toString().isEmpty()) {
                if (!editTextColonia.getText().toString().isEmpty()) {
                    if (!editTextCiudad.getText().toString().isEmpty()) {
                        if (!editTextEmpresa.getText().toString().isEmpty()) {
                            if (!editTextActividad.getText().toString().isEmpty()) {
                                if (!editTextNumEmpleados.getText().toString().isEmpty()) {
                                    if (!editTextRFC.getText().toString().isEmpty()) {
                                        if (!editTextAntiguedad.getText().toString().isEmpty()) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean confirmoNivelEstudios() {
        if (spnNivelEstudios.getSelectedItemPosition() != 0) {
            return true;
        } else {
            return false;
        }
    }

    //método para establecer los datos en los spinners y los textview
    private void asignarDatos(DatosModificarPerfil datosModificarPerfil) {
        if (datosModificarPerfil.getIdNivelEstudios() == 0) { spnNivelEstudios.setSelection(8); } else { spnNivelEstudios.setSelection(datosModificarPerfil.getIdNivelEstudios()); }
        if (datosModificarPerfil.getCalle() != null) editTextCalle.setText(datosModificarPerfil.getCalle());
        if (datosModificarPerfil.getColonia() != null) editTextColonia.setText(datosModificarPerfil.getColonia());
        if (datosModificarPerfil.getCiudad() != null) editTextCiudad.setText(datosModificarPerfil.getCiudad());
        if (datosModificarPerfil.getEmpresa() != null) editTextEmpresa.setText(datosModificarPerfil.getEmpresa());
        if (datosModificarPerfil.getActividad() != null) editTextActividad.setText(datosModificarPerfil.getActividad());
        if (datosModificarPerfil.getNumEmpleados() != 0) editTextNumEmpleados.setText(String.valueOf(datosModificarPerfil.getNumEmpleados()));
        if (datosModificarPerfil.getRfc() != null) editTextRFC.setText(datosModificarPerfil.getRfc());
        if (datosModificarPerfil.getAntiguedad() != 0) editTextAntiguedad.setText(String.valueOf(datosModificarPerfil.getAntiguedad()));
        if (datosModificarPerfil.getRutaImagen() != null) Sesion.getUsuario().getDatosUsuario().setRutaImagen(datosModificarPerfil.getRutaImagen());
    }

}
