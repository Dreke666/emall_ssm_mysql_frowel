package com.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * ��ҳ������
 */
public class PageUtil {

	/**
	 * ��ȡ��ҳ����
	 * @param total �ܼ�¼��
	 * @param page ��ǰҳ��
	 * @param size ÿҳ����
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getPageHtml(HttpServletRequest request, long total, int page, int size) throws UnsupportedEncodingException{
		if (total <= 0) {
			return null;
		}
		// ������ҳ��
		int pages = (int) (total % size ==0 ? total/size : total /size + 1);
		pages = pages == 0 ? 1 : pages;
		// �����ַ
		String url = request.getRequestURL().toString();
		System.out.println(url);
		// �������
		StringBuilder paramBuilder = new StringBuilder();

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			if(param.indexOf("page") > -1) {
				continue;
			}

			paramBuilder.append("&").append(param).append("=").append(new String(request.getParameter(param).getBytes("ISO8859-1"),"utf-8"));
		}
		System.out.println(paramBuilder);
		// ��ҳ�ַ���
		StringBuilder pageBuilder = new StringBuilder();
		pageBuilder.append("<div class='holder'>");
		// pre
		if (page <= 1) { // ����Ѿ��ǵ�һҳ, pre��ťdisabled
//			pageBuilder.append("<a title='����Top'>Top</a>");
//			pageBuilder.append("<a title='����Top'>Pre</a>");
		}else{
			pageBuilder.append("<a href='").append(url).append("?").append("page=1")
			.append(paramBuilder).append("'>Top</a>");
			pageBuilder.append("<a href='").append(url).append("?").append("page=").append(page-1)
			.append(paramBuilder).append("'>Pre</a>");
		}
		// �м�����ҳ��
		if (pages <= 7) { // ȫ����ʾ
			for (int i = 1; i <= pages; i++) {
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, i));
			}
		}else{ // ��ʾ����
			if (page<4 || page>pages-3) { // 1 2 3 ... pages-2 pages-1 pages
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, 1));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, 2));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, 3));
				pageBuilder.append(" ... ");
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, pages-2));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, pages-1));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, pages));
			}else{	// 1 2 ... page-1 page page+1 ... pages-1 pages
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, 1));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, 2));
				pageBuilder.append(" ... ");
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, page-1));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, page));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, page+1));
				pageBuilder.append(" ... ");
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, pages-1));
				pageBuilder.append(packPageItem(url, paramBuilder.toString(), page, pages));
			}
		}
		// next
		if (page >= pages) { // ����Ѿ������һҳ, pre��ťdisabled
//			pageBuilder.append("<a title='����Last'>Next</a>");
//			pageBuilder.append("<a title='����Last'>Last</a>");
		}else{
			pageBuilder.append("<a href='").append(url).append("?").append("page=").append(page+1)
			.append(paramBuilder).append("'>Next</a>");
			pageBuilder.append("<a href='").append(url).append("?").append("page=").append(pages)
			.append(paramBuilder).append("'>Last</a>");
		}
		pageBuilder.append("</div>");
		return pageBuilder.toString();
	}

	/**
	 * ��װ��ҳ��
	 * @param url
	 * @param params
	 * @param page
	 * @param i
	 * @return
	 */
	private static String packPageItem(String url, String params, int page, int i) {
		StringBuilder pageBuilder = new StringBuilder();
		if (i == page) {
			pageBuilder.append("<a class='jp-current'>").append(i).append("</a>");
		}else{
			pageBuilder.append("<a title='The").append(i).append("page' href='").append(url).append("?").append("page=").append(i)
				.append(params).append("'>");pageBuilder.append(i).append("</a>");
		}
		return pageBuilder.toString();
	}

	/**
	 * ��ȡ��ҳ����
	 * @param total �ܼ�¼��
	 * @param page ��ǰҳ��
	 * @param size ÿҳ����
	 * @return
	 */
	public static String getPageTool(HttpServletRequest request, long total, int page, int size){
		long pages = total % size ==0 ? total/size : total /size + 1;
		pages = pages==0 ? 1 : pages;
		String url = request.getRequestURL().toString();
		StringBuilder queryString = new StringBuilder();
		Enumeration<String> enumeration = request.getParameterNames();
		try { // ƴװ�������
			while (enumeration.hasMoreElements()) {
				String element = (String) enumeration.nextElement();
				if(!element.contains("page")) { // ����page����
					queryString.append("&").append(element).append("=").append(java.net.URLEncoder.encode(request.getParameter(element),"UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ƴװ��ҳ����
		StringBuilder buf = new StringBuilder();
		buf.append("<div style='text-align:center;'>\n");
		if (page <= 1) {
			buf.append("<a class='btn btn-info' disabled >Top</a>\n");
		}else{
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(1).append(queryString).append("'>Top</a>\n");
		}
		if (page <= 1) {
			buf.append("<a class='btn btn-info' disabled >pre</a>\n");
		}else {
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(page>1 ? page-1 : 1).append(queryString).append("'>pre</a>\n");
		}
		buf.append("<h2 style='display:inline;'>[").append(page).append("/").append(pages).append("]</h2>\n");
		buf.append("<h2 style='display:inline;'>[").append(total).append("]</h2>\n");
		if (page >= pages) {
			buf.append("<a class='btn btn-info' disabled >next</a>\n");
		}else {
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(page<pages ? page+1 : pages).append(queryString).append("'>next</a>\n");
		}
		if (page >= pages) {
			buf.append("<a class='btn btn-info' disabled >Last</a>\n");
		}else {
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(pages).append(queryString).append("'>Last</a>\n");
		}
		buf.append("<input type='text' class='form-control' style='display:inline;width:60px;' value=''/>");
		buf.append("<a class='btn btn-info' href='javascript:void(0);' onclick='location.href=\"").append(url).append("?page=").append("\"+(this.previousSibling.value)+\"").append(queryString).append("\"'>GO</a>\n");
		buf.append("</div>\n");
		return buf.toString();
	}

}
