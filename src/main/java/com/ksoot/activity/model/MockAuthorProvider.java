package com.ksoot.activity.model;

/** Returns a Mock user named: <b>system</b> from Spring Security context */
public class MockAuthorProvider implements AuthorProvider {

  @Override
  public String getAuthor() {
    return "system";
  }
}
