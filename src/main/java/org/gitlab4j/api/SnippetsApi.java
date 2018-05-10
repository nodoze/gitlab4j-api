package org.gitlab4j.api;

import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.gitlab4j.api.models.Snippet;
import org.gitlab4j.api.models.Visibility;

/**
 * This class provides an entry point to all the GitLab Snippets API project calls.
 */
public class SnippetsApi extends AbstractApi {

	public SnippetsApi(GitLabApi gitLabApi) {
		super(gitLabApi);
	}

    /**
     * Create a new Snippet.
     *
     * @param title the title of the snippet
     * @param fileName the file name of the snippet
     * @param content the content of the snippet
     * @return the created Snippet
     * @throws GitLabApiException if any exception occurs
     */
	public Snippet createSnippet(String title, String fileName, String content) throws GitLabApiException {
		GitLabApiForm formData = new GitLabApiForm()
				.withParam("title", title, true)
				.withParam("file_name", fileName, true)
				.withParam("content", content, true);
		
		Response response = post(Response.Status.CREATED, formData, "snippets");
		return (response.readEntity(Snippet.class));
	}
	
    /**
     * Create a new Snippet.
     *
     * @param title the title of the snippet
     * @param fileName the file name of the snippet
     * @param content the content of the snippet
     * @param visibility the visibility (Public, Internal, Private) of the snippet
     * @param description the description of the snippet
     * @return the created Snippet
     * @throws GitLabApiException if any exception occurs
     */
	public Snippet createSnippet(String title, String fileName, String content, Visibility visibility, String description) throws GitLabApiException {
		GitLabApiForm formData = new GitLabApiForm()
				.withParam("title", title, true)
				.withParam("file_name", fileName, true)
				.withParam("content", content, true)
				.withParam("visibility", visibility)
				.withParam("description", description);
		
		Response response = post(Response.Status.CREATED, formData, "snippets");
		return (response.readEntity(Snippet.class));
	}

    /**
     * Removes Snippet
     *
     * DELETE /snippets/:id
     *
     * @param snippetId the snippet ID to remove
     * @throws GitLabApiException if any exception occurs
     */
	public void deleteSnippet(Integer snippetId) throws GitLabApiException {
		if (snippetId == null) {
			throw new RuntimeException("snippetId can't be null");
		}
		delete(Response.Status.NO_CONTENT, null, "snippets", snippetId);
	}

    /**
     * Get a list of Authenticated User's Snippets.
     *
     * GET /snippets
     *
     * @param downloadContent indicating whether to download the snippet content
     * @return a list of authenticated user's snippets
     * @throws GitLabApiException if any exception occurs
     */
	public List<Snippet> getSnippets(boolean downloadContent) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(), "snippets");
        List<Snippet> snippets = (response.readEntity(new GenericType<List<Snippet>>() {}));
        
        if(downloadContent) {
        	for (Snippet snippet : snippets) {
        		snippet.setContent(getSnippetContent(snippet.getId()));
        	}
        }

        return snippets;
	}
	
    /**
     * Get a list of Authenticated User's Snippets.
     *
     * GET /snippets
     *
     * @return a list of authenticated user's snippets
     * @throws GitLabApiException if any exception occurs
     */
	public List<Snippet> getSnippets() throws GitLabApiException {
		return getSnippets(false);
	}
	
    /**
     * Get a the content of a Snippet
     *
     * GET /snippets/id/raw
     *
     * @param snippetId the snippet ID to remove
     * @return the content of snippet
     * @throws GitLabApiException if any exception occurs
     */
	public String getSnippetContent(Integer snippetId) throws GitLabApiException {
		Response response = get(Response.Status.OK, null, "snippets", snippetId, "raw");
		return (response.readEntity(String.class));
	}

	/**
	 * Get a specific Snippet
	 * 
	 * @param snippetId the snippet ID to remove 
	 * @param downloadContent indicating whether to download the snippet content
	 * @return the snippet with the given id
	 * @throws GitLabApiException if any exception occurs
	 */
	public Snippet getSnippet(Integer snippetId, boolean downloadContent) throws GitLabApiException {
		if (snippetId == null) {
			throw new RuntimeException("snippetId can't be null");
		}
		
		Response response = get(Response.Status.OK, null, "snippets", snippetId);
		Snippet snippet = response.readEntity(Snippet.class);
		
		if(downloadContent) {
			snippet.setContent(getSnippetContent(snippet.getId()));
		}
		
		return snippet;
	}
	
	/**
	 * Get a specific Snippet
	 * 
	 * @param snippetId the snippet ID to remove 
	 * @return the snippet with the given id
	 * @throws GitLabApiException if any exception occurs
	 */
	public Snippet getSnippet(Integer snippetId) throws GitLabApiException {
		return getSnippet(snippetId, false);
	}
	
}