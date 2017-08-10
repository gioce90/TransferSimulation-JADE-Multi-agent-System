package XmlParser;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import transfersimulation.model.goods.Goods;


public class BuyerXmlReader {
	
	Vector<Goods> goods = new Vector<Goods>();
	
	
	public BuyerXmlReader(String file) throws JDOMException, IOException {
		// Creo un SAXBuilder e con esco costruisco un document 
		SAXBuilder builder = new SAXBuilder(); 
		Document document = builder.build(new File(file+".xml")); 
		
		// Prendo la radice 
		Element root = document.getRootElement();
		
		for (Element goodsElem : root.getChildren()) {
			
			Goods g = new Goods();
			g.setBuyer(root.getAttributeValue("buyerName"));
			g.setCodice(goodsElem.getChildText("codice"));
			g.setDescrizione(goodsElem.getChildText("descrizione"));
			g.setDimensione(goodsElem.getChildText("dimensione"));
			g.setQuantità(Float.valueOf(goodsElem.getChildText("quantità")));
			g.setVolume(Float.valueOf(goodsElem.getChildText("volume")));
			g.setTipo(goodsElem.getChildText("tipo"));
			g.setPericolosa(Boolean.valueOf(goodsElem.getChildText("pericolosa")));
			g.setLocationStart(goodsElem.getChildText("locationStart"));
			g.setLocationEnd(goodsElem.getChildText("locationEnd"));
			g.setDateStart(Date.valueOf(goodsElem.getChildText("dateStart")));
			g.setDateLimit(Integer.valueOf(goodsElem.getChildText("dateLimit")));
			g.setNecessità(goodsElem.getChildText("necessità"));

			goods.add(g);
			
		} // chiusura ciclo for
		
	}
	
	
	
	public Vector<Goods> getGoods() {
		return goods;
	}
	
	
	
	public static void main(String[] args) throws JDOMException, IOException { 
		BuyerXmlReader reader = new BuyerXmlReader("Buyer1");
	}
	
	
	
}