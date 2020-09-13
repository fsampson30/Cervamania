package br.com.cervamania.cervamania.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ProtocolFamily;

import br.com.cervamania.cervamania.Controller.TarefaRetornaObjetoCerveja;
import br.com.cervamania.cervamania.Model.BandeirasCervejas;
import br.com.cervamania.cervamania.Model.Cerveja;
import br.com.cervamania.cervamania.Model.CoresCervejas;
import br.com.cervamania.cervamania.Model.ImagensCervejas;
import br.com.cervamania.cervamania.Model.NomesEstilosCervejas;
import br.com.cervamania.cervamania.Model.NomesPaisesCervejas;
import br.com.cervamania.cervamania.R;

public class DetalhesCervejaActivity extends AppCompatActivity {

    private TextView txtNomeCerveja, txtEstiloCerveja, txtDescricaoCerveja, txtCervejariaCerveja, txtTeorCerveja, txtIngredientesCerveja, txtTemperaturaCerveja;
    private ImageView imgGarrafaCerveja, imgPaisCerveja, imgColoracaoCerveja;
    private String nomeCerveja;
    private String codigoCerveja;
    private String codigoCor;
    private String codigoEstiloCerveja;
    private NomesEstilosCervejas nomes = new NomesEstilosCervejas();
    private android.support.v7.widget.Toolbar toolbar;
    private CoresCervejas cores = new CoresCervejas();
    private BandeirasCervejas bandeiras = new BandeirasCervejas();
    private ImagensCervejas imagens = new ImagensCervejas();
    private NomesPaisesCervejas nomesPaisesCervejas = new NomesPaisesCervejas();
    private LinearLayout layout;
    public ProgressBar barraCircular;
    public TextView txtBaixandoInformacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_cerveja);

        savedInstanceState = getIntent().getExtras();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBarDetalhesCervejaActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nomeCerveja = savedInstanceState.getString("nomeCerveja");
        codigoEstiloCerveja = savedInstanceState.getString("codigoEstiloCerveja");

        layout = (LinearLayout) findViewById(R.id.layoutHeaderDetalhesCerveja);

        barraCircular = (ProgressBar) findViewById(R.id.listaCervejasCircularBarDetalhesCerveja);
        txtBaixandoInformacoes = (TextView) findViewById(R.id.txtBaixandoInformaçoesDetalhesCerveja);

        txtNomeCerveja = (TextView) findViewById(R.id.txtDetalhesNomeCerveja);
        txtEstiloCerveja = (TextView) findViewById(R.id.txtDetalhesEstiloCerveja);
        txtDescricaoCerveja = (TextView) findViewById(R.id.txtDetalhesCervejaDescricao);
        txtCervejariaCerveja = (TextView) findViewById(R.id.txtDetalhesCervejaCervejaria);
        txtTeorCerveja = (TextView) findViewById(R.id.txtDetalhesCervejaTeor);
        txtIngredientesCerveja = (TextView) findViewById(R.id.txtDetalhesCervejaIngredientes);
        txtTemperaturaCerveja = (TextView) findViewById(R.id.txtDetalhesCervejaTemperatura);

        imgGarrafaCerveja = (ImageView) findViewById(R.id.imageDetalhesCervejaGarrafa);
        imgPaisCerveja = (ImageView) findViewById(R.id.imageDetalhesPaisCerveja);
        imgColoracaoCerveja = (ImageView) findViewById(R.id.imageDetalhesColoracaoCerveja);

        new TarefaRetornaObjetoCerveja(this).execute(nomeCerveja);

    }

    public void retornoTarefaExterna(final Cerveja cerveja) {
        codigoCerveja = cerveja.getCodigo_cerveja();
        codigoCor = cerveja.getCor();
        txtNomeCerveja.setText(cerveja.getNome_cerveja());
        txtEstiloCerveja.setText(nomes.retornaNomesEstilosCervejas(cerveja.getCodigo_tipo_cerveja()));
        txtDescricaoCerveja.setText(cerveja.getDescricao_cerveja());
        txtCervejariaCerveja.setText(cerveja.getCervejaria());
        txtTeorCerveja.setText(cerveja.getTeor());
        txtIngredientesCerveja.setText(cerveja.getIngredientes());
        txtTemperaturaCerveja.setText(cerveja.getTemperatura());
        imgColoracaoCerveja.setImageResource(cores.retornaImagemCores(cerveja.getCor()));
        imgPaisCerveja.setImageResource(bandeiras.retornaImagemBandeiraCerveja(cerveja.getCodigo_pais_cerveja()));
        imgGarrafaCerveja.setImageResource(imagens.retornaImagemCervejaReduzida(cerveja.getNome_cerveja()));
        layout.setBackgroundColor(getResources().getColor(cores.retornaCoresHexaDecimal(cerveja.getCor())));

        imgPaisCerveja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalhesCervejaActivity.this, nomesPaisesCervejas.retornaNomesEstilosCervejas(cerveja.getCodigo_pais_cerveja()), Toast.LENGTH_SHORT).show();
            }
        });

        imgColoracaoCerveja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalhesCervejaActivity.this, cores.retornaNomesCores(cerveja.getCor()), Toast.LENGTH_SHORT).show();
            }
        });

        imgGarrafaCerveja.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesCervejaActivity.this, VisualizaFotoFamiliaCervejaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("familia", "garrafa");
                bundle.putString("nomeCerveja", cerveja.getNome_cerveja());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
        }

        switch (item.getItemId()) {
            case R.id.menuItemCompartilhar: {
                try {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), imagens.retornaImagemCerveja(nomeCerveja));
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpeg");
                    intent.setPackage("com.whatsapp");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File foto = new File(Environment.getExternalStorageDirectory() + File.separator + "temporario.jpg");
                    Uri uri = FileProvider.getUriForFile(DetalhesCervejaActivity.this, DetalhesCervejaActivity.this.getApplicationContext().getPackageName() + ".provider", foto);
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    if (verificaPermissao()) {
                        foto.createNewFile();
                    }
                    new FileOutputStream(foto).write(bytes.toByteArray());
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_TEXT, "Hora de uma bem gelada!! Enviado por CervaMania.");
                    startActivity(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.menuItemClassificar: {
                Intent intent = new Intent(this, InsereClassificacaoCervejaSelecionadaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nomeCerveja", nomeCerveja);
                bundle.putString("codigoCerveja", codigoCerveja);
                bundle.putString("codigoCor", codigoCor);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }

            case R.id.menuItemRanking: {
                Intent intent = new Intent(this, ListaClassificacaoCervejaSelecionadaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nomeCerveja", nomeCerveja);
                bundle.putString("codigoCerveja", codigoCerveja);
                bundle.putString("codigoCor", codigoCor);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_detalhes_cerveja_activity, menu);
        return true;
    }

    public void clickImgPaisCerveja(View view) {
        Toast.makeText(DetalhesCervejaActivity.this, "MEXICO", Toast.LENGTH_SHORT);
    }

    public boolean verificaPermissao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }


}
