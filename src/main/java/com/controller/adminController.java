package com.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.DaoImpl.*;
import com.model.*;

@Controller
@RequestMapping("/admin")
public class adminController 
{
	@Autowired
	SupplierDaoImpl supplierDaoImpl;
	
	@Autowired
	CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	ProductDaoImpl productDaoImpl;
	
	
	@RequestMapping("/adding")
	public String adding()
	{
		return "adding";
	}
	
@RequestMapping (value="/saveSupp", method=RequestMethod.POST)
@Transactional

public ModelAndView saveSuppData (@RequestParam("sid")int sid, @RequestParam("sname") String sname)
	{
		ModelAndView mv = new ModelAndView();
		Supplier ss = new Supplier();
		ss.setSid(sid);
		ss.setSupplierName(sname);
		supplierDaoImpl.insertSupplier(ss);
		mv.setViewName("adding");
		return mv;
		
	}

@RequestMapping (value="/saveCat", method=RequestMethod.POST)
@Transactional

public ModelAndView saveCatData (@RequestParam("cid")int cid, @RequestParam("cname") String cname)
	{
		ModelAndView mv = new ModelAndView();
		Category cc = new Category();
		cc.setCid(cid);
		cc.setCname(cname);
		categoryDaoImpl.insertCategory(cc);
		mv.setViewName("adding");
		return mv;
		
	}


@RequestMapping (value="saveProduct", method=RequestMethod.POST)
@Transactional
public String saveProd (HttpServletRequest request, @RequestParam("file")MultipartFile file)
{
	Product prod = new Product();
	prod.setPname(request.getParameter("pName"));
	prod.setPrice(Double.parseDouble(request.getParameter("pPrice")));
	prod.setDescription(request.getParameter("pDescription"));
	prod.setStock(Integer.parseInt(request.getParameter("pStock")));
	prod.setCategory(categoryDaoImpl.findByCatId(Integer.parseInt(request.getParameter("pCategory"))));
	prod.setSupplier(supplierDaoImpl.findBySuppId(Integer.parseInt(request.getParameter("pSupplier"))));
	
	String filepath = request.getSession().getServletContext().getRealPath("/");
	String filename = file.getOriginalFilename();
	prod.setImgName(filename);
	productDaoImpl.insertProduct(prod);
	System.out.println("file path "+ filepath);
	
	
	try
	{
		byte imagebyte [] = file.getBytes();
		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream (filepath+"/image"+filename));
		fos.write(imagebyte);
		fos.close();
		}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	return "adding";
}

	@ModelAttribute
	public void loadingDataInPage (Model m)
	{
		m.addAttribute("satList", supplierDaoImpl.retrieve());
		m.addAttribute("catList", categoryDaoImpl.retrieve());
		m.addAttribute("prodList", productDaoImpl.retrieve());
	}
	
	@RequestMapping("/ProductList")
	public ModelAndView prodlist()
	{
		ModelAndView mv = new ModelAndView();
		mv.addObject("prodList", productDaoImpl.retrieve());
		mv.setViewName("AdminProduct");
		return mv;
	}
	
	
	@RequestMapping("/SupplierList")
	public ModelAndView supplist()
	{
		ModelAndView mv = new ModelAndView();
		mv.addObject("satList", supplierDaoImpl.retrieve());
		mv.setViewName("suppAdminList");
		return mv;
	}
	
	@RequestMapping("/CategoryList")
	public ModelAndView catlist()
	{
		ModelAndView mv = new ModelAndView();
		mv.addObject("catList", categoryDaoImpl.retrieve());
		mv.setViewName("categoryAdminList");
		return mv;
	}
	/*	
	@RequestMapping("/DeleteSupplier/{sid}")
	public String deleteSupplier(@PathVariable("sid")int sid)
	{
		supplierDaoImpl.deleteSupp(sid);
		return "redirect:/SupplierList?del";
	}

	@RequestMapping("/DeleteCategory/{cid}")
	public String deleteCategory(@PathVariable("cid")int cid)
	{
		categoryDaoImpl.deleteCat(cid);
		return "redirect:/CategoryList?del";
	}
	
	@RequestMapping("/DeleteProduct/{pid}")
	public String deleteProduct(@PathVariable("pid")int pid)
	{
		productDaoImpl.deleteProd(pid);
		return "redirect:/ProductList?del";
	}
	
	
	
	@RequestMapping("/UpdateProduct")
	public ModelAndView updateProduct(@RequestParam("pid")int pid)
	{
		ModelAndView mv=new ModelAndView();
		Product p=productDaoImpl.findByPId(pid);
		mv.addObject("prod",p);
		mv.addObject("cList", categoryDaoImpl.retrieve());
		mv.addObject("sList", supplierDaoImpl.retrieve());
		mv.setViewName("UpdateProduct");
		return mv;
	}
	
	@RequestMapping(value="/UpdateProduct",method=RequestMethod.POST)
	@Transactional
	public String updateProd(HttpServletRequest request, @RequestParam("file") MultipartFile file)
	{
		String pid=request.getParameter("pid");
		Product prod=new Product();
		prod.setPid(Integer.parseInt(pid));
		prod.setPname(request.getParameter("pName"));
		prod.setPrice(Double.parseDouble(request.getParameter("pPrice")));
		prod.setDescription(request.getParameter("pDescription"));
		prod.setStock(Integer.parseInt(request.getParameter("pStock")));
		String cat=request.getParameter("pCategory");
		String sat=request.getParameter("pSupplier");
		
		prod.setCategory(categoryDaoImpl.findByCatId(Integer.parseInt(cat)));
		prod.setSupplier(supplierDaoImpl.findBySuppId(Integer.parseInt(sat)));
		
		String filepath = "C:/Users/HP/workspace/MobileShopFrontend/src/main/webapp/resources1/Product_Image/";
		String filename = file.getOriginalFilename();
		prod.setImgName(filename);
		productDaoImpl.update(prod);
		System.out.println("File Path:"+filepath);
		
		try{
				byte imagebyte[]=file.getBytes();
				BufferedOutputStream fos=new BufferedOutputStream(new FileOutputStream(filepath+filename));
				fos.write(imagebyte);
				fos.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return "redirect:/ProductList?update";
	}
	
	*/
}



