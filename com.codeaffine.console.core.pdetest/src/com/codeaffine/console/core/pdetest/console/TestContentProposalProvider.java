package com.codeaffine.console.core.pdetest.console;

import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMANDS;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;

class TestContentProposalProvider implements ContentProposalProvider {

  @Override
  public Proposal[] getContentProposals() {
    Collection<Proposal> result = newHashSet();
    for( String command : COMMANDS ) {
      result.add( new Proposal( command, command, command + " info", new TestImageDescriptor() ) );
    }
    return toArray( result, Proposal.class );
  }

  @Override
  public KeyStroke getActivationKeySequence() {
    return KeyStroke.getInstance( SWT.MOD1, SWT.SPACE );
  }
}