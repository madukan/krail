/*
 * Copyright (c) 2015. David Sowerby
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package uk.q3c.krail.core.user.opt;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.q3c.krail.core.user.opt.cache.OptionCache;
import uk.q3c.krail.core.user.opt.cache.OptionCacheKey;
import uk.q3c.krail.core.user.profile.UserHierarchy;
import uk.q3c.krail.i18n.TestLabelKey;
import uk.q3c.krail.util.KrailCodeException;

import javax.annotation.Nonnull;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.q3c.krail.core.user.profile.RankOption.*;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({})
public class DefaultOptionTest {

    DefaultOption option;
    MockContext contextObject;
    MockContext2 contextObject2;
    Class<MockContext> context = MockContext.class;
    Class<MockContext2> context2 = MockContext2.class;
    @Mock
    private UserHierarchy defaultHierarchy;
    @Mock
    private UserHierarchy hierarchy2;
    @Mock
    private OptionCache optionCache;
    private OptionKey optionKey1;
    private OptionKey optionKey2;

    @Before
    public void setup() {
        contextObject = new MockContext();
        option = new DefaultOption(optionCache, defaultHierarchy);
        optionKey1 = new OptionKey(context, TestLabelKey.key1, "q");
        optionKey2 = new OptionKey(context2, TestLabelKey.key1, "q");
    }

    @Test
    public void init_from_class() {
        //given

        //when
        option.init(context);
        //then
        assertThat(option.getContext()).isEqualTo(MockContext.class);
    }

    @Test
    public void init_from_object() {
        //given

        //when
        option.init(contextObject);
        //then
        assertThat(option.getContext()).isEqualTo(MockContext.class);
    }

    @Test(expected = KrailCodeException.class)
    public void init_not_done() {
        //given

        //when
        option.get(5, TestLabelKey.key1);
        //then
        //exception
    }

    @Test
    public void set_simplest() {
        //given
        option.init(context);
        when(defaultHierarchy.rankName(0)).thenReturn("specific");
        OptionCacheKey cacheKey = new OptionCacheKey(defaultHierarchy, SPECIFIC_RANK, 0, optionKey1);
        //when
        option.set(5, TestLabelKey.key1, "q");
        //then
        verify(optionCache).write(cacheKey, Optional.of(5));
    }

    @Test
    public void set_with_hierarchy() {
        //given
        option.init(context);
        when(hierarchy2.rankName(0)).thenReturn("specific");
        OptionCacheKey cacheKey = new OptionCacheKey(hierarchy2, SPECIFIC_RANK, 0, optionKey1);
        //when
        option.set(5, hierarchy2, TestLabelKey.key1, "q");
        //then
        verify(optionCache).write(cacheKey, Optional.of(5));
    }

    @Test
    public void set_with_all_args() {
        //given
        option.init(context);
        when(hierarchy2.rankName(2)).thenReturn("specific");
        OptionKey optionKey2 = new OptionKey(context2, TestLabelKey.key1, "q");
        OptionCacheKey cacheKey = new OptionCacheKey(hierarchy2, SPECIFIC_RANK, 2, optionKey2);
        //when
        option.set(5, hierarchy2, 2, context2, TestLabelKey.key1, "q");
        //then
        verify(optionCache).write(cacheKey, Optional.of(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_with_all_args_rank_too_low() {
        //given
        option.init(context);
        when(hierarchy2.rankName(2)).thenReturn("specific");
        OptionKey optionKey2 = new OptionKey(context, TestLabelKey.key1, "q");
        OptionCacheKey cacheKey = new OptionCacheKey(hierarchy2, SPECIFIC_RANK, 2, optionKey2);
        //when
        option.set(5, hierarchy2, -1, context2, TestLabelKey.key1, "q");
        //then
    }

    @Test
    public void get_simplest() {
        //given
        option.init(context);
        when(defaultHierarchy.highestRankName()).thenReturn("high");
        OptionCacheKey cacheKey = new OptionCacheKey(defaultHierarchy, HIGHEST_RANK, optionKey1);
        when(optionCache.get(Optional.of(5),cacheKey)).thenReturn(Optional.of(8));
        //when
        Integer actual = option.get(5, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(8);
    }

    @Test
    public void get_with_hierarchy() {
        //given
        option.init(context);
        when(hierarchy2.highestRankName()).thenReturn("high");
        OptionCacheKey cacheKey = new OptionCacheKey(hierarchy2, HIGHEST_RANK, optionKey1);
        when(optionCache.get(Optional.of(5),cacheKey)).thenReturn(Optional.of(8));
        //when
        Integer actual = option.get(5, hierarchy2, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(8);
    }

    @Test
    public void get_with_context() {
        //given
        option.init(context);
        when(defaultHierarchy.highestRankName()).thenReturn("high");
        OptionCacheKey cacheKey = new OptionCacheKey(defaultHierarchy, HIGHEST_RANK, optionKey2);
        when(optionCache.get(Optional.of(5),cacheKey)).thenReturn(Optional.of(8));
        //when
        Integer actual = option.get(5, context2, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(8);
    }

    @Test
    public void get_with_all_args() {
        //given
        option.init(context);
        when(hierarchy2.highestRankName()).thenReturn("high");
        OptionCacheKey cacheKey = new OptionCacheKey(hierarchy2, HIGHEST_RANK, optionKey2);
        when(optionCache.get(Optional.of(5),cacheKey)).thenReturn(Optional.of(8));
        //when
        Integer actual = option.get(5, hierarchy2, context2, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(8);
    }

    @Test
    public void get_none_found() {
        //given
        option.init(context);
        when(defaultHierarchy.highestRankName()).thenReturn("high");
        OptionCacheKey cacheKey = new OptionCacheKey(defaultHierarchy, HIGHEST_RANK, optionKey2);
        when(optionCache.get(Optional.of(5),cacheKey)).thenReturn(Optional.empty());
        //when
        Integer actual = option.get(5, context2, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(5);
    }

    @Test
    public void get_lowest() {
        //given
        option.init(context);
        when(defaultHierarchy.lowestRankName()).thenReturn("low");
        OptionCacheKey cacheKey = new OptionCacheKey(defaultHierarchy, LOWEST_RANK, optionKey2);
        when(optionCache.get(Optional.of(5), cacheKey)).thenReturn(Optional.of(20));
        //when
        Integer actual = option.getLowestRanked(5, defaultHierarchy, context2, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(20);
    }

    @Test
    public void delete() {
        //given
        option.init(context);
        when(hierarchy2.rankName(1)).thenReturn("specific");
        OptionCacheKey cacheKey = new OptionCacheKey(hierarchy2, SPECIFIC_RANK, 1, optionKey2);
        when(optionCache.delete(cacheKey)).thenReturn(Optional.of(3));
        //when
        Object actual = option.delete(hierarchy2, 1, context2, TestLabelKey.key1, "q");
        //then
        assertThat(actual).isEqualTo(Optional.of(3));
        verify(optionCache).delete(cacheKey);
    }

    static class MockContext implements OptionContext {

        @Nonnull
        @Override
        public Option getOption() {
            return null;
        }
    }

    static class MockContext2 implements OptionContext {

        @Nonnull
        @Override
        public Option getOption() {
            return null;
        }
    }
}