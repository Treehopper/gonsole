package com.codeaffine.gonsole.internal.interpreter;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.StringWriter;
import java.util.MissingResourceException;

import org.eclipse.jgit.pgm.Command;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.pgm.opt.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class CommandLineParser {

  private final PgmResourceBundle pgmResourceBundle;

  public CommandLineParser() {
    pgmResourceBundle = new PgmResourceBundle();
  }

  public boolean isRecognized( String...commands ) {
    boolean result = false;
    try {
      new CmdLineParser( new CommandInfo() ).parseArgument( commands );
      result = true;
    } catch( CmdLineException notRecognized ) {
    }
    return result;
  }

  public CommandInfo parse( String... commands ) {
    CommandInfo result = new CommandInfo();
    try {
      new CmdLineParser( result ).parseArgument( commands );
    } catch( CmdLineException e ) {
      throw new RuntimeException( e );
    }
    return result;
  }

  public String getUsage( String command ) {
    CommandInfo commandInfo = new CommandInfo();
    CmdLineParser parser = new CmdLineParser( commandInfo );
    try {
      parser.parseArgument( command );
    } catch( Exception ignore ) {
    }
    return getUsage( commandInfo );
  }

  private String getUsage( CommandInfo commandInfo ) {
    StringWriter result = new StringWriter();
    String usage = getCommandUsage( commandInfo );
    if( !usage.isEmpty() ) {
      result.append( usage );
      result.append( "\n" );
    }
    CmdLineParser parser = new CmdLineParser( commandInfo.getCommand() );
    parser.printSingleLineUsage( result, pgmResourceBundle.getResourceBundle() );
    if( result.getBuffer().length() > 0 ) {
      result.append( "\n\n" );
    }
    try {
      parser.printUsage( result, pgmResourceBundle.getResourceBundle() );
    } catch( MissingResourceException ignore ) {
      // [rh] workaround for Bug 457208: [pgm] resource bundle entry for "usage_bareClone" of the Clone command does not exist
      //      https://bugs.eclipse.org/bugs/show_bug.cgi?id=457208
    }
    return result.toString();
  }

  private String getCommandUsage( CommandInfo commandInfo ) {
    String result = "";
    TextBuiltin command = commandInfo.getCommand();
    if( command != null ) {
      Command commandAnnotation = command.getClass().getAnnotation( Command.class );
      if( commandAnnotation != null && !isNullOrEmpty( commandAnnotation.usage() ) ) {
        result = pgmResourceBundle.getResourceBundle().getString( commandAnnotation.usage() );
      }
    }
    return result;
  }
}