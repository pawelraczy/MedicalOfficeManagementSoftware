import java.awt.*;

/**
 * Small Medical Office Management software
 * @author stayclassy
 * @version 1.00
 */

public class MVCMedicalOffice {

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run() 
			{
				MedicalOfficeView theView = new MedicalOfficeView();
				MedicalOfficeModel theModel = new MedicalOfficeModel();
				MedicalOfficeController theController = new MedicalOfficeController(theView, theModel);
				theView.setVisible(true);	
			}
		});
	}
}
